import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    FunctionType parentCtor = mock(FunctionType.class);
    FunctionType childCtor = mock(FunctionType.class);
    SubclassType type = SubclassType.INHERITS;

    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    ArgumentCaptor<String> superClassPropertyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<FunctionType> constructorCaptor = ArgumentCaptor.forClass(FunctionType.class);
    ArgumentCaptor<FunctionType> prototypeCaptor = ArgumentCaptor.forClass(FunctionType.class);

    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    // Check that the "superClass_" property was set on the child constructor.
    when(childCtor.getPrototype()).thenReturn(parentNode);
    when(parentNode.defineDeclaredProperty(superClassPropertyCaptor.capture(),
        anyFunctionType(), any()))
        .thenReturn(parentNode);

    assertThat(superClassPropertyCaptor.getValue()).isEqualTo("superClass_");

    // Check that the "constructor" property was set on the child constructor's prototype.
    when(parentCtor.getPrototype()).thenReturn(parentPrototype);
    when(parentPrototype.defineDeclaredProperty(constructorCaptor.capture(),
        anyFunctionType(), any()))
        .thenReturn(parentPrototype);

    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    FunctionType childPrototype = childCtor.getPrototype();
    assertThat(childPrototype.getDeclaredProperties()).containsExactly(
        property("constructor", childCtor));

    // Check that the "constructor" property was set on the child constructor's prototype.
    FunctionType constructor = constructorCaptor.getValue();
    assertThat(constructor.getPrototype()).isEqualTo(childPrototype);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node callNode = createCallNode("goog.provide", "X");
    String className = codingConvention.extractClassNameIfProvide(callNode, null);

    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testExtractClassNameIfRequire() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node callNode = createCallNode("goog.require", "X");
    String className = codingConvention.extractClassNameIfRequire(callNode, null);

    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    String exportPropertyFunction = codingConvention.getExportPropertyFunction();

    assertThat(exportPropertyFunction).isEqualTo("goog.exportProperty");
  }

  @Test
  public void testGetExportSymbolFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    String exportSymbolFunction = codingConvention.getExportSymbolFunction();

    assertThat(exportSymbolFunction).isEqualTo("goog.exportSymbol");
  }

  @Test
  public void testGetObjectLiteralCast() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node callNode = createCallNode("goog.reflect.object", "toString");
    ObjectLiteralCast objectLiteralCast = codingConvention.getObjectLiteralCast(
        new NodeTraversal(), callNode);

    assertThat(objectLiteralCast).isNotNull();
    assertThat(objectLiteralCast.getTypeName()).isEqualTo("toString");
    assertThat(objectLiteralCast.getObjectNode()).isNotNull();
  }

  @Test
  public void testIsPropertyTestFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node callNode = createCallNode("goog.isDef", "someVariable");

    boolean isPropertyTestFunction = codingConvention.isPropertyTestFunction(callNode);

    assertThat(isPropertyTestFunction).isTrue();
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node callNode = createCallNode("goog.addDependency",
        ImmutableList.of("MyType", "MyDependency"));

    List<String> typeNames = codingConvention.identifyTypeDeclarationCall(callNode);

    assertThat(typeNames).containsExactly("MyType");
  }

  @Test
  public void testApplySingletonGetter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    FunctionType functionType = mock(FunctionType.class);
    FunctionType getterType = mock(FunctionType.class);
    ObjectType objectType = mock(ObjectType.class);

    codingConvention.applySingletonGetter(functionType, getterType, objectType);

    ArgumentCaptor<String> getterNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<FunctionType> getterTypeCaptor = ArgumentCaptor.forClass(FunctionType.class);
    ArgumentCaptor<ObjectType> objectTypeCaptor = ArgumentCaptor.forClass(ObjectType.class);

    codingConvention.applySingletonGetter(functionType, getterType, objectType);

    codingConvention.applySingletonGetter(functionType, getterType, objectType);

    // Check that the "getInstance" property was set on the functionType.
    when(getterType.getName()).thenReturn("getInstance");
    when(objectType.getName()).thenReturn("InstanceType");
    when(functionType.defineDeclaredProperty(getterNameCaptor.capture(),
        getterTypeCaptor.capture(), any()))
        .thenReturn(functionType);

    assertThat(getterNameCaptor.getValue()).isEqualTo("getInstance");
    FunctionType getter = getterTypeCaptor.getValue();
    assertThat(getter.getName()).isEqualTo("getInstance");

    // Check that the "instance_" property was set on the functionType.
    when(objectType.getName()).thenReturn("InstanceType");
    when(functionType.defineDeclaredProperty("instance_", objectTypeCaptor.capture(),
        any()))
        .thenReturn(functionType);

    assertThat(objectTypeCaptor.getValue()).isEqualTo(objectType);
  }

  private Node createCallNode(String functionName, Object... arguments) {
    SourceName sourceName = new SourceName("Test.java");
    Node callNode = new Node(Token.CALL, sourceName, 0);
    Node callName = new Node(Token.GETPROP, sourceName, 0, functionName);
    callNode.setFirstChild(callName);

    for (Object argument : arguments) {
      Node argumentNode = new Node(Token.UNDEFINED, sourceName, 0);
      callNode.addChild(argumentNode);
      when(argumentNode.getValue()).thenReturn(argument);
    }

    return callNode;
  }
}