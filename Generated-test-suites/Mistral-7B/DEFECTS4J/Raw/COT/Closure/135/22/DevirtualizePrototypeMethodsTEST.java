import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.DefinitionSite;
import com.google.javascript.jscomp.Definition;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.rhino.NodeUtil;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;

public class DevirtualizePrototypeMethodsTest {

 @Test
 public void testDevirtualizePrototypeMethods() throws Exception {
 Compiler compiler = new Compiler();
 CompilerPass devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);

 // Create test case for process method
 Node root = Node.objectLiteral();
 Node externs = Node.objectLiteral();
 devirtualizePrototypeMethods.process(externs, root);
 assertNotNull(root);

 // Create test case for isPrototypeMethodDefinition method
 Node prototypeMethodDefinitionNode = Node.newExpr("A.prototype.accumulate = function(value) { this.total += value; return this.total }");
 assert(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(prototypeMethodDefinitionNode));

 // Create test case for getRewrittenMethodName method
 String originalMethodName = "accumulate";
 String newMethodName = DevirtualizePrototypeMethods.getRewrittenMethodName(originalMethodName);
 assertEquals("JSCompiler_StaticMethods_accumulate", newMethodName);

 // Create test case for rewriteDefinition method
 Node definitionNode = prototypeMethodDefinitionNode.getFirstChild().getNext();
 DevirtualizePrototypeMethods devirtualizePrototypeMethodsInstance = new DevirtualizePrototypeMethods(compiler);
 devirtualizePrototypeMethodsInstance.rewriteDefinition(definitionNode, newMethodName);
 Node functionNode = definitionNode.getLastChild();
 Node newNameNode = functionNode.getFirstChild();
 assertEquals(newMethodName, newNameNode.getString());

 // Create test case for rewriteCallSites method
 Node callSiteNode = Node.newCall(Node.newGetProp(Node.newThis(), newNameNode), Node.newNumber(2));
 Collection<Definition> singleSiteDefinitions = new SimpleDefinitionFinder(compiler).getDefinitionsReferencedAt(callSiteNode);
 Preconditions.checkState(singleSiteDefinitions.size() > 0);
 Definition definition = singleSiteDefinitions.iterator().next();
 Node definitionSiteNode = definition.getNode();
 Node objectNode = callSiteNode.getFirstChild();
 Node newMethodNode = definitionSiteNode.getLastChild();
 Node newThisNode = newNode("this");
 Node newCallSiteNode = Node.newCall(newMethodNode, newThisNode, Node.newNumber(2));
 assertEquals(newCallSiteNode.toString(), "JSCompiler_StaticMethods_accumulate(this, 2)");

 // Create test case for isEligibleDefinition method (single definition)
 Node singleDefinitionNode = Node.newExpr("A.prototype.accumulate = function(value) { this.total += value; return this.total }");
 DevirtualizePrototypeMethods.isEligibleDefinition(singleDefinitionNode, null);

 // Create test case for isEligibleDefinition method (multiple definitions)
 Node multipleDefinitionNode1 = Node.newExpr("A.prototype.accumulate = function(value) { this.total += value; return this.total }");
 Node multipleDefinitionNode2 = Node.newExpr("A.prototype.accumulate = function(value) { this.total += value * value; return this.total }");
 assertFalse(DevirtualizePrototypeMethods.isEligibleDefinition(multipleDefinitionNode1, null));

 // Create test case for isEligibleDefinition method (exported definition)
 Node exportedDefinitionNode = Node.newExpr("exports.accumulate = function(value) { this.total += value; return this.total }");
 assertFalse(DevirtualizePrototypeMethods.isEligibleDefinition(exportedDefinitionNode, null));

 // Create test case for isEligibleDefinition method (unused definition)
 Node unusedDefinitionNode = Node.newExpr("A.prototype.unusedAccumulate = function(value) { this.total += value; return this.total }");
 assertFalse(DevirtualizePrototypeMethods.isEligibleDefinition(unusedDefinitionNode, null));

 // Create test case for isEligibleDefinition method (accessed outside function call context)
 Node accessedOutsideContextNode = Node.newGetProp(Node.newString("A"), newNameNode);
 assertFalse(DevirtualizePrototypeMethods.isEligibleDefinition(accessedOutsideContextNode, null));

 // Create test case for isEligibleDefinition method (multiple definitions at call sites)
 Node multipleDefinitionsAtCallSitesNode = Node.newGetProp(Node.newThis(), newNameNode);
 Node multipleDefinitionsNode1 = Node.newExpr("A.prototype.accumulate = function(value) { this.total += value; return this.total }");
 Node multipleDefinitionsNode2 = Node.newExpr("B.prototype.accumulate = function(value) { this.total += value; return this.total }");
 SimpleDefinitionFinder simpleDefinitionFinder = new SimpleDefinitionFinder(compiler);
 List<DefinitionSite> definitionSites = simpleDefinitionFinder.findDefinitionSites(multipleDefinitionsAtCallSitesNode);
 Preconditions.checkState(definitionSites.size() > 0);
 DefinitionSite definitionSite = definitionSites.get(0);
 Collection<Definition> singleSiteDefinitionsAtCallSite = simpleDefinitionFinder.getDefinitionsReferencedAt(definitionSite.getNode());
 Preconditions.checkState(singleSiteDefinitionsAtCallSite.size() > 1);
 assertFalse(DevirtualizePrototypeMethods.isEligibleDefinition(multipleDefinitionsNode1, definitionSite));

 // Create test case for isEligibleDefinition method (definition in module loaded before first use)
 Node definitionInModuleLoadedBeforeFirstUseNode = Node.newExpr("B.prototype.accumulate = function(value) { this.total += value; return this.total }");
 Node callSiteInModuleLoadedAfterDefinitionNode = Node.newCall(multipleDefinitionsAtCallSitesNode, Node.newNumber(2));
 JSModule definitionModule = NodeUtil.getModule(multipleDefinitionsNode1);
 JSModule callModule = NodeUtil.getModule(callSiteInModuleLoadedAfterDefinitionNode);
 assertFalse(DevirtualizePrototypeMethods.isEligibleDefinition(definitionInModuleLoadedBeforeFirstUseNode, null));
 }
}