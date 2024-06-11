package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

	private FunctionBuilder functionBuilder;
	private JSTypeRegistry registry;
	private String name;
	private Node sourceNode;
	private Node parametersNode;
	private JSType returnType;
	private ObjectType typeOfThis;
	private String templateTypeName;
	private boolean inferredReturnType;
	private boolean isConstructor;
	private boolean isNativeType;


	@Before
	public void setUp() throws Exception {
		registry = new JSTypeRegistry();
		functionBuilder = new FunctionBuilder(registry);
		name = "testFunction";
		sourceNode = new Node();
		parametersNode = new Node();
		returnType = registry.getNativeType(JSTypeNative.VOID_TYPE);
		typeOfThis = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
		templateTypeName = "TestTemplate";
		inferredReturnType = true;
		isConstructor = true;
		isNativeType = true;

	}

	@Test
	public void testWithName() {
		FunctionBuilder builder = functionBuilder.withName(name);
		assertEquals(name, builder.build().getReferenceName());
	}

	@Test
	public void testWithSourceNode() {
		FunctionBuilder builder = functionBuilder.withSourceNode(sourceNode);
		assertEquals(sourceNode, builder.build().getSource());
	}

	@Test
	public void testWithParams() {

		FunctionBuilder builder = functionBuilder.withParams(new FunctionParamBuilder(registry));
		assertEquals(new FunctionParamBuilder(registry).build(), builder.build().getParametersNode());
	}

	@Test
	public void testWithParamsNode() {
		FunctionBuilder builder = functionBuilder.withParamsNode(parametersNode);
		assertEquals(parametersNode, builder.build().getParametersNode());
	}

	@Test
	public void testWithReturnType() {
		FunctionBuilder builder = functionBuilder.withReturnType(returnType);
		assertEquals(returnType, builder.build().getReturnType());
	}

	@Test
	public void testWithInferredReturnType() {
		FunctionBuilder builder = functionBuilder.withInferredReturnType(returnType);
		assertEquals(returnType, builder.build().getReturnType());
		assertEquals(true, builder.build().isInferredReturnType());
	}

	@Test
	public void testWithTypeOfThis() {
		FunctionBuilder builder = functionBuilder.withTypeOfThis(typeOfThis);
		assertEquals(typeOfThis, builder.build().getTypeOfThis());
	}

	@Test
	public void testWithTemplateName() {
		FunctionBuilder builder = functionBuilder.withTemplateName(templateTypeName);
		assertEquals(templateTypeName, builder.build().getTemplateTypeName());
	}

	@Test
	public void testForConstructor() {
		FunctionBuilder builder = functionBuilder.forConstructor();
		assertEquals(true, builder.build().isConstructor());
	}

	@Test
	public void testForNativeType() {
		functionBuilder = functionBuilder.forNativeType();
		assertEquals(true, functionBuilder.build().isNativeObjectType());
	}

	@Test
	public void testCopyFromOtherFunction() {
		FunctionType otherType = new FunctionType(registry, name, sourceNode,
				new ArrowType(registry, parametersNode, returnType, inferredReturnType),
				typeOfThis, templateTypeName, isConstructor, isNativeType);

		FunctionBuilder builder = functionBuilder.copyFromOtherFunction(otherType);
		assertEquals(otherType.getReferenceName(), builder.build().getReferenceName());
		assertEquals(otherType.getSource(), builder.build().getSource());
		assertEquals(otherType.getParametersNode(), builder.build().getParametersNode());
		assertEquals(otherType.getReturnType(), builder.build().getReturnType());
		assertEquals(otherType.getTypeOfThis(), builder.build().getTypeOfThis());
		assertEquals(otherType.getTemplateTypeName(), builder.build().getTemplateTypeName());
		assertEquals(otherType.isConstructor(), builder.build().isConstructor());
		assertEquals(otherType.isNativeObjectType(), builder.build().isNativeObjectType());

	}

	@Test
	public void testBuild() {
		FunctionType functionType = functionBuilder.withName(name)
				.withSourceNode(sourceNode)
				.withParams(new FunctionParamBuilder(registry))
				.withReturnType(returnType)
				.withInferredReturnType(returnType)
				.withTypeOfThis(typeOfThis)
				.withTemplateName(templateTypeName)
				.forConstructor()
				.forNativeType()
				.build();

		assertEquals(name, functionType.getReferenceName());
		assertEquals(sourceNode, functionType.getSource());
		assertEquals(new FunctionParamBuilder(registry).build(), functionType.getParametersNode());
		assertEquals(returnType, functionType.getReturnType());
		assertEquals(typeOfThis, functionType.getTypeOfThis());
		assertEquals(templateTypeName, functionType.getTemplateTypeName());
		assertEquals(true, functionType.isConstructor());
		assertEquals(true, functionType.isNativeObjectType());

	}
}