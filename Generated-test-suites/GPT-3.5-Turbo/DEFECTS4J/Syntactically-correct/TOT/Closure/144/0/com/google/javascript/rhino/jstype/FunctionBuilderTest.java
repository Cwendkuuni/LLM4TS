import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {
   private FunctionBuilder functionBuilder;

   @Before
   public void setUp() {
      functionBuilder = new FunctionBuilder(new JSTypeRegistry());
   }

   @Test
   public void testName() {
      FunctionBuilder result = functionBuilder.withName("testFunction");
      
      assertEquals("testFunction", result.build().getName());
   }

   @Test
   public void testSourceNode() {
      Node sourceNode = new Node();
      FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
      
      assertEquals(sourceNode, result.build().getSourceNode());
   }

   @Test
   public void testParams() {
      Node parametersNode = new Node();
      FunctionParamBuilder params = new FunctionParamBuilder();
      params.withName("param1").withType(JSTypeRegistry.STRING_TYPE);
      parametersNode.addChildToBack(params.build());
      FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
      
      assertEquals(parametersNode, result.build().getParametersNode());
   }

   @Test
   public void testReturnType() {
      JSType returnType = JSTypeRegistry.NUMBER_TYPE;
      FunctionBuilder result = functionBuilder.withReturnType(returnType);
      
      assertEquals(returnType, result.build().getReturnType());
   }

   @Test
   public void testInferredReturnType() {
      JSType returnType = JSTypeRegistry.NUMBER_TYPE;
      FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);

      assertTrue(result.build().isInferredReturnType());
   }

   @Test
   public void testTypeOfThis() {
      ObjectType typeOfThis = new ObjectType();
      FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);

      assertEquals(typeOfThis, result.build().getTypeOfThis());
   }

   @Test
   public void testTemplateName() {
      String templateTypeName = "template";
      FunctionBuilder result = functionBuilder.withTemplateName(templateTypeName);

      assertEquals(templateTypeName, result.build().getTemplateTypeName());
   }

   @Test
   public void testForConstructor() {
      FunctionBuilder result = functionBuilder.forConstructor();

      assertTrue(result.build().isConstructor());
   }

   @Test
   public void testForNativeType() {
      FunctionBuilder result = functionBuilder.forNativeType();

      assertTrue(result.build().isNativeType());
   }

   @Test
   public void testCopyFromOtherFunction() {
      FunctionType otherType = new FunctionType(registry, "other", new Node(), new ArrowType(registry), new ObjectType(), "X", true, true);
      FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);

      assertEquals("other", result.build().getName());
      assertEquals(otherType.getSource(), result.build().getSourceNode());
      assertEquals(otherType.getParametersNode(), result.build().getParametersNode());
      assertEquals(otherType.getReturnType(), result.build().getReturnType());
      assertEquals(otherType.getTypeOfThis(), result.build().getTypeOfThis());
      assertEquals(otherType.getTemplateTypeName(), result.build().getTemplateTypeName());
      assertEquals(otherType.isConstructor(), result.build().isConstructor());
      assertEquals(otherType.isNativeObjectType(), result.build().isNativeType());
   }

   @Test
   public void testBuild() {
      FunctionType expectedType = new FunctionType(registry, "testFunction", new Node(), new ArrowType(registry), new ObjectType(), "testTemplate", true, false);

      FunctionType result = functionBuilder.withName("testFunction")
         .withSourceNode(new Node())
         .withParamsNode(new Node())
         .withReturnType(JSTypeRegistry.NULL_TYPE)
         .withTypeOfThis(new ObjectType())
         .withTemplateName("testTemplate")
         .forConstructor()
         .build();

      assertEquals(expectedType.getName(), result.getName());
      assertEquals(expectedType.getSourceNode(), result.getSourceNode());
      assertEquals(expectedType.getParametersNode(), result.getParametersNode());
      assertEquals(expectedType.getReturnType(), result.getReturnType());
      assertEquals(expectedType.getTypeOfThis(), result.getTypeOfThis());
      assertEquals(expectedType.getTemplateTypeName(), result.getTemplateTypeName());
      assertEquals(expectedType.isConstructor(), result.isConstructor());
      assertEquals(expectedType.isNativeObjectType(), result.isNativeType());
   }
}