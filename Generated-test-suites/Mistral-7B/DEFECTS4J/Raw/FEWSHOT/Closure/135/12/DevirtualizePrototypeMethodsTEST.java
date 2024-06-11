package com.google.javascript.jscomp.test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilationTestHelper;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() {
    String sourceCode = "(\n" +
        "  function A() {}\n" +
        "  A.prototype.accumulate = function(value) { return this.total += value; }\n" +
        "  var a = new A();\n" +
        "  var total = a.accumulate(2);\n" +
        ")\n";

    CompilationTestHelper helper = new CompilationTestHelper("DevirtualizePrototypeMethodsTest", sourceCode);
    Compiler compiler = helper.getCompiler();
    CompilerPass pass = new DevirtualizePrototypeMethods(compiler);

    Node externs = helper.getExterns();
    Node root = helper.getMainCode();
    pass.process(externs, root);

    Node totalNode = helper.getRootNode().lookupVariable("total");
    Node accumulateNode = helper.getRootNode().lookupVariable("accumulate");
    Node aNode = helper.getRootNode().lookupVariable("a");

    // Check that the total variable is assigned to the result of the now static method call
    Node totalAssignNode = helper.getRootNode().lookupVariable("total").getInitializer();
    Preconditions.checkState(totalAssignNode.getNumChildren() == 2);
    Node newAccumulateNode = totalAssignNode.getChild(0);
    Preconditions.checkState(newAccumulateNode.getNumChildren() == 4);
    Node accumulateCallNode = newAccumulateNode.getChild(0);
    Preconditions.checkState(accumulateCallNode.getNumChildren() == 3);
    Node accumulateMethodNameNode = accumulateCallNode.getChild(0);
    String accumulateMethodName = accumulateMethodNameNode.getString();
    Preconditions.checkState(accumulateMethodName.equals("JSCompiler_StaticMethods_accumulate"));
    Node aArgumentsNode = accumulateCallNode.getChild(1);
    Node aNodeCallArgNode = aArgumentsNode.getChild(0);
    Preconditions.checkState(aNodeCallArgNode.getNodeType() == Node.NODE_TYPE_NAME);
    Preconditions.checkState(aNodeCallArgNode.getString().equals("a"));

    // Check that the accumulate method is rewritten as a static method
    Node accumulateDefinitionNode = helper.getRootNode().lookupFunction("accumulate");
    Preconditions.checkState(accumulateDefinitionNode != null);
    Node accumulateFunctionNode = accumulateDefinitionNode.getBody();
    Preconditions.checkState(accumulateFunctionNode != null);
    Preconditions.checkState(accumulateFunctionNode.getNumChildren() == 1);
    Node accumulateFunctionBodyNode = accumulateFunctionNode.getChild(0);
    Preconditions.checkState(accumulateFunctionBodyNode != null);
    Preconditions.checkState(accumulateFunctionBodyNode.getNumChildren() > 0);
    Node accumulateSelfNode = accumulateFunctionBodyNode.getFirstChild();
    Preconditions.checkState(accumulateSelfNode != null);
    Preconditions.checkState(accumulateSelfNode.getNodeType() == Node.NODE_TYPE_VAR);
    String accumulateSelfName = accumulateSelfNode.getFirstChild().getString();
    Preconditions.checkState(accumulateSelfName.equals("self"));

    // Check that the accumulate method body references 'self' instead of 'this'
    Node accumulateTotalNode = accumulateFunctionBodyNode.getLastChild();
    Preconditions.checkState(accumulateTotalNode != null);
    Preconditions.checkState(accumulateTotalNode.getNumChildren() == 2);
    Node accumulateThisNode = accumulateTotalNode.getChild(0);
    Preconditions.checkState(accumulateThisNode == null || accumulateThisNode.getNodeType() == Token.TOKEN_INVALID);
    Node accumulateSelfAddNode = accumulateTotalNode.getChild(0);
    Preconditions.checkState(accumulateSelfAddNode.getNumChildren() == 3);
    Node accumulateSelfNode = accumulateSelfAddNode.getChild(0);
    Preconditions.checkState(accumulateSelfNode != null && accumulateSelfNode.getNodeType() == Node.NODE_TYPE_NAME);
    Preconditions.checkState(accumulateSelfNode.getString().equals(accumulateSelfName));
    Node accumulateValueNode = accumulateSelfAddNode.getChild(1);
    Preconditions.checkState(accumulateValueNode != null);
    Preconditions.checkState(accumulateValueNode.getNodeType() == Node.NODE_TYPE_NAME);
    Node accumulateAssignNode = accumulateSelfAddNode.getChild(2);
    Preconditions.checkState(accumulateAssignNode != null && accumulateAssignNode.getNodeType() == Token.TOKEN_ASSIGN);
    Node accumulateTotalNode2 = accumulateAssignNode.getRightHandSide();
    Preconditions.checkState(accumulateTotalNode2 != null && accumulateTotalNode2.getNodeType() == Node.NODE_TYPE_NAME);
    String accumulateTotalName = accumulateTotalNode2.getString();
    Preconditions.checkState(accumulateTotalName.equals("total"));

    // Check that the accumulate method type is updated to accept 'self' as first argument
    FunctionType accumulateType = (FunctionType) accumulateDefinitionNode.getType();
    Preconditions.checkState(accumulateType != null);
    List<JSType> accumulateParameterTypes = accumulateType.getParameters();
    Preconditions.checkState(accumulateParameterTypes.size() == 1);
    JSType accumulateSelfType = accumulateParameterTypes.get(0);
    Preconditions.checkState(accumulateSelfType != null && accumulateSelfType.isThisType());
    JSType accumulateThisType = accumulateType.getTypeOfThis();
    Preconditions.checkState(accumulateThisType != null && accumulateThisType.isUnknownType());

    // Check that the 'this' reference in the original code no longer exists
    Node aPrototypeNode = helper.getRootNode().getFirstChild();
    Node aPrototypeAccumulateNode = aPrototypeNode.getLastChild();
    Preconditions.checkState(aPrototypeAccumulateNode != null);
    Preconditions.checkState(aPrototypeAccumulateNode.getNumChildren() == 2);
    Node aPrototypeAccumulateMethodNameNode = aPrototypeAccumulateNode.getChild(0);
    String aPrototypeAccumulateMethodName = aPrototypeAccumulateMethodNameNode.getString();
    Preconditions.checkState(aPrototypeAccumulateMethodName.equals("accumulate"));
    Node aPrototypeAccumulateFunctionNode = aPrototypeAccumulateNode.getChild(1);
    Preconditions.checkState(aPrototypeAccumulateFunctionNode != null);
    Preconditions.checkState(aPrototypeAccumulateFunctionNode.getNumChildren() > 0);
    Node aPrototypeAccumulateFunctionBodyNode = aPrototypeAccumulateFunctionNode.getChild(0);
    Preconditions.checkState(aPrototypeAccumulateFunctionBodyNode != null);
    Node aPrototypeAccumulateThisNode = aPrototypeAccumulateFunctionBodyNode.getFirstChild();
    Preconditions.checkState(aPrototypeAccumulateThisNode != null && aPrototypeAccumulateThisNode.getNodeType() == Node.NODE_TYPE_THIS);
    Node aPrototypeAccumulateThisAssignedNode = aPrototypeAccumulateFunctionBodyNode.getLastChild();
    Preconditions.checkState(aPrototypeAccumulateThisAssignedNode != null && aPrototypeAccumulateThisAssignedNode.getNodeType() == Token.TOKEN_ASSIGN);
    Node aPrototypeAccumulateThisNode2 = aPrototypeAccumulateThisAssignedNode.getLeftHandSide();
    Preconditions.checkState(aPrototypeAccumulateThisNode2 != null && aPrototypeAccumulateThisNode2.getNodeType() == Node.NODE_TYPE_NAME);
    String aPrototypeAccumulateThisName = aPrototypeAccumulateThisNode2.getString();
    Preconditions.checkState(aPrototypeAccumulateThisName.equals("this"));
    assertNull(helper.getTypeChecker().getTypeOf(aPrototypeAccumulateThisNode));

    // Check that the 'this' reference in the static method call is replaced with 'a'
    Node aNodeCallSiteNode = helper.getRootNode().getFirstChild().getLastChild();
    Preconditions.checkState(aNodeCallSiteNode != null);
    Preconditions.checkState(aNodeCallSiteNode.getNumChildren() == 3);
    Node aNodeCallSiteMethodNameNode = aNodeCallSiteNode.getChild(0);
    String aNodeCallSiteMethodName = aNodeCallSiteMethodNameNode.getString();
    Preconditions.checkState(aNodeCallSiteMethodName.equals("accumulate"));
    Node aNodeCallSiteArgumentsNode = aNodeCallSiteNode.getChild(1);
    Preconditions.checkState(aNodeCallSiteArgumentsNode != null);
    Node aNodeCallSiteArgumentsThisNode = aNodeCallSiteArgumentsNode.getFirstChild();
    Preconditions.checkState(aNodeCallSiteArgumentsThisNode != null && aNodeCallSiteArgumentsThisNode.getNodeType() == Node.NODE_TYPE_THIS);
    Node aNodeCallSiteArgumentsANode = aNodeCallSiteArgumentsNode.getChild(1);
    Preconditions.checkState(aNodeCallSiteArgumentsANode != null && aNodeCallSiteArgumentsANode.getNodeType() == Node.NODE_TYPE_NAME);
    String aNodeCallSiteArgumentsAName = aNodeCallSiteArgumentsANode.getString();
    Preconditions.checkState(aNodeCallSiteArgumentsAName.equals("a"));
  }
}