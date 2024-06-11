import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.JSError;

public class CrossModuleMethodMotionTest {
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    IdGenerator idGenerator = new IdGenerator();
    boolean canModifyExterns = true;
    CrossModuleMethodMotion motion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    
    // Create sample input data
    Node externRoot = new Node();
    Node root = new Node();
    
    // Test case 1: No modules
    motion.process(externRoot, root);
    // No methods should be moved as there are no modules
    assertEquals(0, compiler.getErrors().size());
    
    // Test case 2: Less than 2 modules
    JSModuleGraph moduleGraph1 = new JSModuleGraph();
    compiler.setModuleGraph(moduleGraph1);
    motion.process(externRoot, root);
    // No methods should be moved as there are less than 2 modules
    assertEquals(0, compiler.getErrors().size());
    
    // Test case 3: NullPointerException in moveMethods()
    JSModuleGraph moduleGraph2 = new JSModuleGraph();
    moduleGraph2.addModule(new JSModule("module1"));
    moduleGraph2.addModule(new JSModule("module2"));
    compiler.setModuleGraph(moduleGraph2);
    motion.process(externRoot, root);
    assertEquals(1, compiler.getErrors().size());
    assertTrue(compiler.getErrors().get(0).getType().equals(JSError.make(NULL_COMMON_MODULE_ERROR).getType()));
    
    // Test case 4: Successful moveMethods()
    JSModule module1 = new JSModule("module1");
    JSModule module2 = new JSModule("module2");
    moduleGraph2.addModule(module1);
    moduleGraph2.addModule(module2);
    Node protoNode = new Node(Token.OBJECTLIT);
    Node funcNode = new Node(Token.FUNCTION);
    Node propNode = new Node(Token.STRING, "name1");
    propNode.addChildToBack(funcNode);
    protoNode.addChildToFront(propNode);
    Property p = new Property("name1", propNode, module1, 1);
    p.setPrototype(protoNode);
    NameInfo nameInfo = new NameInfo("name1", module2);
    nameInfo.addDeclaration(p);
    analyzer.getAllNameInfo().add(nameInfo);
    motion.process(externRoot, root);
    assertEquals(0, compiler.getErrors().size());
    
    // Test case 5: Existing stub methods
    JSModule module3 = new JSModule("module3");
    JSModuleGraph moduleGraph3 = new JSModuleGraph();
    moduleGraph3.addModule(module1);
    moduleGraph3.addModule(module3);
    compiler.setModuleGraph(moduleGraph3);
    // Creating a stub method
    Node declarations = compiler.parseSyntheticCode(STUB_DECLARATIONS);
    compiler.getNodeForCodeInsertion(null).addChildrenToFront(declarations.removeChildren());
    motion.process(externRoot, root);
    assertEquals(0, compiler.getErrors().size());
  }
  
  @Test
  public void testMoveMethods() {
    AbstractCompiler compiler = new AbstractCompiler();
    IdGenerator idGenerator = new IdGenerator();
    boolean canModifyExterns = true;
    CrossModuleMethodMotion motion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    
    // Create sample input data
    Collection<NameInfo> allNameInfo = new ArrayList<>();
    Property prop = new Property("name1", new Node(topNull), new JSModule(module1), 1);
    Node propNode = new Node(Token.STRING, "name1");
    Node functionNode = new Node(Token.FUNCTION);
    propNode.addChildToBack(functionNode);
    Node protoNode = new Node(Token.OBJECTLIT);
    protoNode.addChildToFront(propNode);
    prop.setPrototype(protoNode);
    prop.setValue(functionNode);
    NameInfo nameInfo = new NameInfo("name1", new JSModule(module2));
    nameInfo.addDeclaration(prop);
    allNameInfo.add(nameInfo);
    motion.moveMethods(allNameInfo);
    assertEquals(1, compiler.getErrors().size());
    assertTrue(compiler.getErrors().get(0).getType().equals(JSError.make(NULL_COMMON_MODULE_ERROR).getType()));
    
    // Test case 1: Moving methods successfully
    JSModule module1 = new JSModule("module1");
    JSModule module2 = new JSModule("module2");
    moduleGraph2.addModule(module1);
    moduleGraph2.addModule(module2);
    NameInfo nameInfo1 = new NameInfo("name1", module2);
    nameInfo1.addDeclaration(prop);
    allNameInfo.add(nameInfo1);
    motion.moveMethods(allNameInfo);
    assertEquals(0, compiler.getErrors().size());
    
    // Test case 2: Property is not a function
    prop.setValue(new Node(Token.STRING, "stringVal"));
    motion.moveMethods(allNameInfo);
    assertEquals(0, compiler.getErrors().size());
    
    // Test case 3: Moving property across modules is not possible
    JSModuleGraph moduleGraph3 = new JSModuleGraph();
    moduleGraph3.addModule(module1);
    moduleGraph3.addModule(new JSModule("module3"));
    compiler.setModuleGraph(moduleGraph3);
    prop.setValue(functionNode);
    motion.moveMethods(allNameInfo);
    assertEquals(0, compiler.getErrors().size());
    
    // Test case 4: Property is a getter or setter -> Do not move
    Node getterNode = new Node(Token.GETTER_DEF);
    getterNode.addChildToBack(functionNode);
    prop.setValue(getterNode);
    motion.moveMethods(allNameInfo);
    assertEquals(0, compiler.getErrors().size());
    
    // Test case 5: Property is not referenced -> Do not move
    nameInfo1.setReferenced(false);
    motion.moveMethods(allNameInfo);
    assertEquals(0, compiler.getErrors().size());
    
    // Test case 6: CodeChange report for successful method movement
    nameInfo1.setReferenced(true);
    motion.moveMethods(allNameInfo);
    assertEquals(1, compiler.getCodeChange());
    
    // Test case 7: Successful addition of stub declarations
    IdGenerator idGenerator2 = new IdGenerator();
    idGenerator2.newId();
    motion.moveMethods(allNameInfo);
    assertEquals(1, compiler.getCodeChange());
  }

  @Test
  public void testIdGenerator() {
    IdGenerator idGenerator = new IdGenerator();
    
    // Test case 1: hasGeneratedAnyIds() returns false initially
    assertFalse(idGenerator.hasGeneratedAnyIds());
    
    // Test case 2: newId() returns unique ids
    int id1 = idGenerator.newId();
    assertTrue(idGenerator.hasGeneratedAnyIds());
    int id2 = idGenerator.newId();
    assertTrue(id2 != id1);
  }

}