import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FunctionBuilderTest {

 private FunctionBuilder functionBuilder;
 private JSTypeRegistry registry;
 private Node sourceNode;
 private Node parametersNode;
 private FunctionParamBuilder functionParamBuilder;
 private ObjectType objectType;
 private ArrowType arrowType;
 private FunctionType functionType;

 @Before
 public void setUp() {
 registry = mock(JSTypeRegistry.class);
 functionBuilder = new FunctionBuilder(registry);
 sourceNode = mock(Node.class);
 parametersNode = mock(Node.class);
 functionParamBuilder = mock(FunctionParamBuilder.class);
 objectType = mock(ObjectType.class);
 arrowType = mock(ArrowType.class);
 functionType = mock(FunctionType.class);
 }

 @Test
 public void testFunctionBuilderWithNameAndSourceNode() {
 when(registry.createFunctionType(sourceNode)).thenReturn(functionType);
 functionBuilder.withName("testFunction")
 .withSourceNode(sourceNode)
 .build();
 assertEquals("testFunction", functionType.getReferenceName());
 }

 @Test
 public void testFunctionBuilderWithNameSourceNodeAndParams() {
 when(registry.createFunctionType(sourceNode)).thenReturn(functionType);
 when(functionParamBuilder.build()).thenReturn(parametersNode);
 functionBuilder.withName("testFunction")
 .withSourceNode(sourceNode)
 .withParams(functionParamBuilder)
 .build();
 assertEquals(parametersNode, functionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithNameSourceNodeAndReturnType() {
 when(registry.createFunctionType(sourceNode)).thenReturn(functionType);
 functionBuilder.withName("testFunction")
 .withSourceNode(sourceNode)
 .withReturnType(objectType)
 .build();
 assertEquals(objectType, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameSourceNodeParamsAndReturnType() {
 when(registry.createFunctionType(sourceNode)).thenReturn(functionType);
 when(functionParamBuilder.build()).thenReturn(parametersNode);
 functionBuilder.withName("testFunction")
 .withSourceNode(sourceNode)
 .withParams(functionParamBuilder)
 .withReturnType(objectType)
 .build();
 assertEquals(parametersNode, functionType.getParametersNode());
 assertEquals(objectType, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameSourceNodeParamsReturnTypeAndTypeOfThis() {
 when(registry.createFunctionType(sourceNode)).thenReturn(functionType);
 when(functionParamBuilder.build()).thenReturn(parametersNode);
 functionBuilder.withName("testFunction")
 .withSourceNode(sourceNode)
 .withParams(functionParamBuilder)
 .withReturnType(objectType)
 .withTypeOfThis(objectType)
 .build();
 assertEquals(objectType, functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithNameSourceNodeParamsReturnTypeTypeOfThisAndTemplateTypeName() {
 when(registry.createFunctionType(sourceNode)).thenReturn(functionType);
 when(functionParamBuilder.build()).thenReturn(parametersNode);
 functionBuilder.withName("testFunction")
 .withSourceNode(sourceNode)
 .withParams(functionParamBuilder)
 .withReturnType(objectType)
 .withTypeOfThis(objectType)
 .withTemplateName("testTemplate")
 .build();
 assertEquals("testTemplate", functionType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 functionBuilder.forConstructor();
 assertEquals(true, functionType.isConstructor());
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 functionBuilder.forNativeType();
 assertEquals(true, functionType.isNativeObjectType());
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherFunctionType = mock(FunctionType.class);
 when(otherFunctionType.getReferenceName()).thenReturn("otherFunction");
when(otherFunctionType.getSource()).thenReturn(sourceNode);
when(otherFunctionType.getParametersNode()).thenReturn(parametersNode);
when(otherFunctionType.getReturnType()).thenReturn(objectType);
when(otherFunctionType.getTypeOfThis()).thenReturn(objectType);
when(otherFunctionType.getTemplateTypeName()).thenReturn("otherTemplate");
when(otherFunctionType.isConstructor()).thenReturn(true);
when(otherFunctionType.isNativeObjectType()).thenReturn(true);

functionBuilder.copyFromOtherFunction(otherFunctionType);
assertEquals("otherFunction", functionType.getReferenceName());
assertEquals(sourceNode, functionType.getSource());
assertEquals(parametersNode, functionType.getParametersNode());
assertEquals(objectType, functionType.getReturnType());
assertEquals(objectType, functionType.getTypeOfThis());
assertEquals("otherTemplate", functionType.getTemplateTypeName());
assertEquals(true, functionType.isConstructor());
assertEquals(true, functionType.isNativeObjectType());
}

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 JSType inferredReturnType = mock(JSType.class);
 functionBuilder.withInferredReturnType(inferredReturnType);
 assertEquals(inferredReturnType, functionType.getReturnType());
 assertEquals(true, functionType.isInferredReturnType());
 }
}