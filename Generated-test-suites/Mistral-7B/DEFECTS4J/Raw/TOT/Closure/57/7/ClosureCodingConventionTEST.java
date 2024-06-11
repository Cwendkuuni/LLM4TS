import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor",
        new ObjectType("Object"), new ObjectType("Object"), null);
    FunctionType childCtor = new FunctionType("ChildCtor",
        new ObjectType("Object"), new ObjectType("Object"), null);
    SubclassType type = SubclassType.INHERITS;
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, type);
    assertEquals("ParentCtor", childCtor.getPrototype().getName());
    assertEquals(parentCtor, childCtor.getPrototype().getConstructor());
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = Node.newNode(Token.CALL, new SourceName("testFile.js"), 0);
    Node callName = Node.newNode(Token.NAME, new SourceName("testFile.js"), 1);
    callName.setString("goog.inherits");
    Node subclass = Node.newNode(Token.NAME, new SourceName("testFile.js"), 2);
    subclass.setString("SubClass");
    Node superclass = Node.newNode(Token.NAME, new SourceName("testFile.js"), 3);
    superclass.setString("SuperClass");
    callName.setNext(subclass);
    superclass.setNext(callName);
    superclass.setNext(superclass.getFirstChild().getNext());
    SubclassRelationship relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(callNode, null);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("SubClass", relationship.getSubclass().getString());
    assertEquals("SuperClass", relationship.getSuperclass().getString());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newNode(Token.CALL, new SourceName("testFile.js"), 0);
    Node callName = Node.newNode(Token.GETPROP, new SourceName("testFile.js"), 0);
    callName.setString("goog.provide");
    Node target = Node.newNode(Token.NAME, new SourceName("testFile.js"), 1);
    target.setString("X");
    callName.setNext(target);
    assertEquals("X", new ClosureCodingConvention().extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode(Token.CALL, new SourceName("testFile.js"), 0);
    Node callName = Node.newNode(Token.GETPROP, new SourceName("testFile.js"), 0);
    callName.setString("goog.require");
    Node target = Node.newNode(Token.NAME, new SourceName("testFile.js"), 1);
    target.setString("X");
    callName.setNext(target);
    assertEquals("X", new ClosureCodingConvention().extractClassNameIfRequire(node, null));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty",
        new ClosureCodingConvention().getExportPropertyFunction());
  }

  @Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol",
        new ClosureCodingConvention().getExportSymbolFunction());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newNode(
        Token.CALL, new SourceName("testFile.js"), 0);
    Node callName = Node.newNode(Token.NAME, new SourceName("testFile.js"), 0);
    callName.setString("goog.addDependency");
    Node typeArray = Node.newNode(Token.ARRAYLIT, new SourceName("testFile.js"), 0);
    Node typeName1 = Node.newNode(Token.STRING, new SourceName("testFile.js"), 0);
    typeName1.setString("Type1");
    Node typeName2 = Node.newNode(Token.STRING, new SourceName("testFile.js"), 1);
    typeName2.setString("Type2");
    typeArray.setNext(typeName1);
    typeArray.setNext(typeName2);
    callName.setNext(typeArray);
    List<String> typeNames = new ClosureCodingConvention()
        .identifyTypeDeclarationCall(node);
    assertEquals(2, typeNames.size());
    assertEquals("Type1", typeNames.get(0));
    assertEquals("Type2", typeNames.get(1));
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(new ClosureCodingConvention().isSuperClassReference("superClass_"));
    assertFalse(new ClosureCodingConvention().isSuperClassReference("otherProperty"));
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = Node.newNode(Token.CALL, new SourceName("testFile.js"), 0);
    Node callArg = Node.newNode(Token.NAME, new SourceName("testFile.js"), 0);
    callArg.setString("goog.addSingletonGetter");
    Node getterName = Node.newNode(Token.NAME, new SourceName("testFile.js"), 1);
    getterName.setString("getterName");
    callArg.setNext(getterName);
    Node returnedValue = Node.newNode(Token.NAME, new SourceName("testFile.js"), 2);
    getterName.setNext(returnedValue);
    assertEquals("getterName",
        new ClosureCodingConvention().getSingletonGetterClassName(callNode));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType getterType = new FunctionType("Getter",
        new ObjectType("Object"), new ObjectType("Object"), null);
    ObjectType objectType = new ObjectType("Object");
    new ClosureCodingConvention().applySingletonGetter(new FunctionType("Class",
        new ObjectType("Object"), null, null), getterType, objectType);
    assertTrue(getterType.hasProperty("getInstance"));
    assertTrue(getterType.hasProperty("instance_"));
  }
}