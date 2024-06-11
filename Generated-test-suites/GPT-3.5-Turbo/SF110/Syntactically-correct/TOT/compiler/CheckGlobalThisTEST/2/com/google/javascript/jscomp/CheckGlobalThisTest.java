import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis();
  }

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ShouldReturnFalse() {
    // Given
    Node functionNode = createFunctionWithConstructorAnnotation();

    // When
    boolean result = checkGlobalThis.shouldTraverse(functionNode);

    // Then
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
    // Given
    Node functionNode = createFunctionWithThisAnnotation();

    // When
    boolean result = checkGlobalThis.shouldTraverse(functionNode);

    // Then
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ShouldReturnFalse() {
    // Given
    Node functionNode = createFunctionWithOverrideAnnotation();

    // When
    boolean result = checkGlobalThis.shouldTraverse(functionNode);

    // Then
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideBlock_ShouldReturnFalse() {
    // Given
    Node functionNode = createFunctionInsideBlock();

    // When
    boolean result = checkGlobalThis.shouldTraverse(functionNode);

    // Then
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideScript_ShouldReturnFalse() {
    // Given
    Node functionNode = createFunctionInsideScript();

    // When
    boolean result = checkGlobalThis.shouldTraverse(functionNode);

    // Then
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideName_ShouldReturnFalse() {
    // Given
    Node functionNode = createFunctionInsideName();

    // When
    boolean result = checkGlobalThis.shouldTraverse(functionNode);

    // Then
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideAssign_ShouldReturnFalse() {
    // Given
    Node functionNode = createFunctionInsideAssign();

    // When
    boolean result = checkGlobalThis.shouldTraverse(functionNode);

    // Then
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideObjectLiteralKey_ShouldReturnFalse() {
    // Given
    Node functionNode = createFunctionInsideObjectLiteralKey();

    // When
    boolean result = checkGlobalThis.shouldTraverse(functionNode);

    // Then
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideConstructorLend_ShouldReturnFalse() {
    // Given
    Node functionNode = createFunctionInsideConstructorLend();

    // When
    boolean result = checkGlobalThis.shouldTraverse(functionNode);

    // Then
    assertFalse(result);
  }

  private Node createFunctionWithConstructorAnnotation() {
    // Create a function annotated with @constructor
    // function Foo() {}
    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = Node.newString(Token.NAME, "Foo");
    functionNode.addChildToBack(nameNode);
    JSDocInfoBuilder jsDocInfoBuilder = new JSDocInfoBuilder(true);
    jsDocInfoBuilder.recordConstructor();
    JSDocInfo jsDocInfo = jsDocInfoBuilder.build();
    functionNode.setJSDocInfo(jsDocInfo);

    return functionNode;
  }

  private Node createFunctionWithThisAnnotation() {
    // Create a function annotated with @this
    // function bar() {}
    // bar.prototype.foo = function() {}
    // new bar().foo();
    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = Node.newString(Token.NAME, "bar");
    functionNode.addChildToBack(nameNode);
    JSDocInfoBuilder jsDocInfoBuilder = new JSDocInfoBuilder(true);
    jsDocInfoBuilder.recordThis();
    JSDocInfo jsDocInfo = jsDocInfoBuilder.build();
    functionNode.setJSDocInfo(jsDocInfo);

    Node fooNode = new Node(Token.STRING);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(fooNode);
    Node.prototype = blockNode;

    Node newBarNode = new Node(Token.NEW);
    Node callNode = new Node(Token.CALL);
    Node barNode = new Node(Token.NAME);
    barNode.addChildToBack(Node.newString(Token.NAME, "bar"));
    callNode.addChildToBack(barNode);
    newBarNode.addChildToBack(callNode);

    Node referenceNode = new Node(Token.STRING);
    referenceNode.setString("foo");
    Node getPropNode = new Node(Token.GETPROP);
    getPropNode.addChildToBack(newBarNode);
    getPropNode.addChildToBack(referenceNode);

    return getPropNode;
  }

  private Node createFunctionWithOverrideAnnotation() {
    // Create a function annotated with @override
    // bar.prototype.foo = function() {}
    Node barNode = new Node(Token.NAME);
    barNode.addChildToBack(Node.newString(Token.NAME, "bar"));

    Node fooNode = new Node(Token.STRING);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(fooNode);
    Node.prototype = blockNode;

    Node newBarNode = new Node(Token.NEW);

    Node referenceNode = new Node(Token.STRING);
    referenceNode.setString("foo");
    Node getPropNode = new Node(Token.GETPROP);
    getPropNode.addChildToBack(newBarNode);
    getPropNode.addChildToBack(referenceNode);

    return getPropNode;
  }

  private Node createFunctionInsideBlock() {
    // Create a function inside a block
    // var a = {foo: function() {}};
    Node aNode = new Node(Token.NAME);
    aNode.addChildToBack(Node.newString(Token.NAME, "a"));

    Node fooNode = new Node(Token.STRING);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(fooNode);

    Node objLiteral = new Node(Token.OBJECTLIT);
    Node keyNode = new Node(Token.STRING);
    keyNode.setString("foo");
    objLiteral.addChildToBack(keyNode);
    objLiteral.addChildToBack(blockNode);

    return objLiteral;
  }

  private Node createFunctionInsideScript() {
    // Create a function inside a script
    // function foo() {}
    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = Node.newString(Token.NAME, "foo");
    functionNode.addChildToBack(nameNode);

    return functionNode;
  }

  private Node createFunctionInsideName() {
    // Create a function inside a name
    // var a = function() {};
    Node aNode = new Node(Token.NAME);
    aNode.addChildToBack(Node.newString(Token.NAME, "a"));

    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = Node.newString(Token.NAME, "");
    functionNode.addChildToBack(nameNode);

    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(aNode);
    assignNode.addChildToBack(functionNode);

    return assignNode;
  }

  private Node createFunctionInsideAssign() {
    // Create a function inside an assign
    // var a;
    // (a = function() {}).foo();
    Node aNode = new Node(Token.NAME);
    aNode.addChildToBack(Node.newString(Token.NAME, "a"));

    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = Node.newString(Token.NAME, "");
    functionNode.addChildToBack(nameNode);

    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(aNode);
    assignNode.addChildToBack(functionNode);

    Node fooNode = new Node(Token.STRING);
    Node getPropNode = new Node(Token.GETPROP);
    getPropNode.addChildToBack(assignNode);
    getPropNode.addChildToBack(fooNode);

    return getPropNode;
  }

  private Node createFunctionInsideObjectLiteralKey() {
    // Create a function inside an object literal key
    // var a = {foo: function() {}};
    Node fooNode = new Node(Token.STRING);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(fooNode);

    Node aNode = new Node(Token.NAME);
    aNode.addChildToBack(Node.newString(Token.NAME, "a"));

    Node objLiteral = new Node(Token.OBJECTLIT);
    Node keyNode = new Node(Token.STRING);
    keyNode.setString("foo");
    objLiteral.addChildToBack(keyNode);
    objLiteral.addChildToBack(blockNode);

    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(aNode);
    assignNode.addChildToBack(objLiteral);

    return assignNode;
  }

  private Node createFunctionInsideConstructorLend() {
    // Create a function inside a constructor lend
    // var a = {foo: function () {}};
    // bar.prototype.lends(a).foo = function () {};
    Node fooNode = new Node(Token.STRING);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(fooNode);

    Node aNode = new Node(Token.NAME);
    aNode.addChildToBack(Node.newString(Token.NAME, "a"));

    Node objLiteral = new Node(Token.OBJECTLIT);
    Node keyNode = new Node(Token.STRING);
    keyNode.setString("foo");
    objLiteral.addChildToBack(keyNode);
    objLiteral.addChildToBack(blockNode);

    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(aNode);
    assignNode.addChildToBack(objLiteral);

    Node lendsNode = new Node(Token.CALL);
    Node fooNode2 = new Node(Token.STRING);
    fooNode2.setString("foo");
    lendsNode.addChildToBack(fooNode2);
    Node propNode = new Node(Token.GETPROP);
    propNode.addChildToBack(assignNode);
    propNode.addChildToBack(lendsNode);

    return propNode;
  }
}