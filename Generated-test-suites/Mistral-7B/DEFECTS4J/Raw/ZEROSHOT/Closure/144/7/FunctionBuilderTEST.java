import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createObject;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createNodeFactory;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.jstype.FunctionBuilder.*;

import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType functionType = functionBuilder
        .withName("add")
        .withParams(FunctionParamBuilder.withName("a").withType(IntType.getInstance()))
        .withParams(FunctionParamBuilder.withName("b").withType(IntType.getInstance()))
        .withReturnType(IntType.getInstance())
        .build();

    FunctionType expectedFunctionType = new FunctionType(registry, "add", null,
        new ArrowType(registry, createArray(new Node[] {
          FunctionParamBuilder.withName("a").withType(IntType.getInstance()),
          FunctionParamBuilder.withName("b").withType(IntType.getInstance())
        }), IntType.getInstance()), null, null, false, false);

    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    Node sourceNode = createObject(createNodeFactory());

    FunctionType functionType = functionBuilder
        .withName("add")
        .withSourceNode(sourceNode)
        .withParams(FunctionParamBuilder.withName("a").withType(IntType.getInstance()))
        .withParams(FunctionParamBuilder.withName("b").withType(IntType.getInstance()))
        .withReturnType(IntType.getInstance())
        .build();

    FunctionType expectedFunctionType = new FunctionType(registry, "add", sourceNode,
        new ArrowType(registry, createArray(new Node[] {
          FunctionParamBuilder.withName("a").withType(IntType.getInstance()),
          FunctionParamBuilder.withName("b").withType(IntType.getInstance())
        }), IntType.getInstance()), null, null, false, false);

    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    ObjectType thisType = ObjectType.getInstance();

    FunctionType functionType = functionBuilder
        .withName("add")
        .withParams(FunctionParamBuilder.withName("a").withType(IntType.getInstance()))
        .withParams(FunctionParamBuilder.withName("b").withType(IntType.getInstance()))
        .withReturnType(IntType.getInstance())
        .withTypeOfThis(thisType)
        .build();

    FunctionType expectedFunctionType = new FunctionType(registry, "add", null,
        new ArrowType(registry, createArray(new Node[] {
          FunctionParamBuilder.withName("a").withType(IntType.getInstance()),
          FunctionParamBuilder.withName("b").withType(IntType.getInstance())
        }), IntType.getInstance()), thisType, null, false, false);

    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType functionType = functionBuilder
        .withName("add")
        .withParams(FunctionParamBuilder.withName("a").withType(IntType.getInstance()))
        .withParams(FunctionParamBuilder.withName("b").withType(IntType.getInstance()))
        .withReturnType(IntType.getInstance())
        .withTemplateName("Add")
        .build();

    FunctionType expectedFunctionType = new FunctionType(registry, "add", null,
        new ArrowType(registry, createArray(new Node[] {
          FunctionParamBuilder.withName("a").withType(IntType.getInstance()),
          FunctionParamBuilder.withName("b").withType(IntType.getInstance())
        }), IntType.getInstance()), null, "Add", false, false);

    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType functionType = functionBuilder
        .withName("add")
        .withParams(FunctionParamBuilder.withName("a").withType(IntType.getInstance()))
        .withParams(FunctionParamBuilder.withName("b").withType(IntType.getInstance()))
        .withReturnType(null)
        .withInferredReturnType(IntType.getInstance())
        .build();

    FunctionType expectedFunctionType = new FunctionType(registry, "add", null,
        new ArrowType(registry, createArray(new Node[] {
          FunctionParamBuilder.withName("a").withType(IntType.getInstance()),
          FunctionParamBuilder.withName("b").withType(IntType.getInstance())
        }), IntType.getInstance()), null, null, false, false);

    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType functionType = functionBuilder
        .withName("Person")
        .forConstructor()
        .build();

    FunctionType expectedFunctionType = new FunctionType(registry, "Person", null,
        null, null, true, false);

    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType functionType = functionBuilder.forNativeType().build();

    FunctionType expectedFunctionType = new FunctionType(registry, null, null,
        null, null, false, true);

    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testCopyFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType originalFunctionType = functionBuilder
        .withName("add")
        .withParams(FunctionParamBuilder.withName("a").withType(IntType.getInstance()))
        .withParams(FunctionParamBuilder.withName("b").withType(IntType.getInstance()))
        .withReturnType(IntType.getInstance())
        .build();

    FunctionType copiedFunctionType = functionBuilder.copyFromOtherFunction(originalFunctionType).build();

    assertEquals(originalFunctionType, copiedFunctionType);
  }
}