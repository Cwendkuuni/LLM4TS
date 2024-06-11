import static org.junit.Assert.*;

import org.junit.Test;

public class ClosureCodingConventionTest {

	@Test
	public void testApplySubclassRelationship() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		FunctionType parentCtor = new FunctionType();
		FunctionType childCtor = new FunctionType();
		SubclassType type = SubclassType.INHERITS;
		convention.applySubclassRelationship(parentCtor, childCtor, type);
		assertTrue(childCtor.getPrototype().hasDeclaredProperty("superClass_"));
		assertTrue(childCtor.getPrototype().hasDeclaredProperty("constructor"));
	}

	@Test
	public void testGetclasssDefinedByCall() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		Node callNode = new Node();
		SubclassRelationship result = convention.getclasssDefinedByCall(callNode);
		assertNull(result);
	}

	@Test
	public void testIsSuperClassReference() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		assertFalse(convention.isSuperClassReference("propertyName"));
	}

	@Test
	public void testExtractClassNameIfProvide() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		Node node = new Node();
		Node parent = new Node();
		String result = convention.extractClassNameIfProvide(node, parent);
		assertNull(result);
	}

	@Test
	public void testExtractClassNameIfRequire() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		Node node = new Node();
		Node parent = new Node();
		String result = convention.extractClassNameIfRequire(node, parent);
		assertNull(result);
	}

	@Test
	public void testGetExportPropertyFunction() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		assertEquals("goog.exportProperty", convention.getExportPropertyFunction());
	}

	@Test
	public void testGetExportSymbolFunction() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		assertEquals("goog.exportSymbol", convention.getExportSymbolFunction());
	}

	@Test
	public void testIdentifyTypeDeclarationCall() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		Node n = new Node();
		assertNull(convention.identifyTypeDeclarationCall(n));
	}

	@Test
	public void testGetAbstractMethodName() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		assertEquals("goog.abstractMethod", convention.getAbstractMethodName());
	}

	@Test
	public void testGetSingletonGetterClassName() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		Node callNode = new Node();
		assertNull(convention.getSingletonGetterClassName(callNode));
	}

	@Test
	public void testApplySingletonGetter() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		FunctionType functionType = new FunctionType();
		FunctionType getterType = new FunctionType();
		ObjectType objectType = new ObjectType();
		convention.applySingletonGetter(functionType, getterType, objectType);
		assertTrue(functionType.hasDeclaredProperty("getInstance"));
		assertTrue(functionType.hasDeclaredProperty("instance_"));
	}

	@Test
	public void testGetGlobalObject() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		assertEquals("goog.global", convention.getGlobalObject());
	}

	@Test
	public void testIsPropertyTestFunction() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		Node call = new Node();
		assertFalse(convention.isPropertyTestFunction(call));
	}

	@Test
	public void testGetObjectLiteralCast() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		NodeTraversal t = new NodeTraversal(null);
		Node callNode = new Node();
		assertNull(convention.getObjectLiteralCast(t, callNode));
	}

	@Test
	public void testIsOptionalParameter() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		Node parameter = new Node();
		assertFalse(convention.isOptionalParameter(parameter));
	}

	@Test
	public void testIsVarArgsParameter() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		Node parameter = new Node();
		assertFalse(convention.isVarArgsParameter(parameter));
	}

	@Test
	public void testIsPrivate() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		assertFalse(convention.isPrivate("name"));
	}

	@Test
	public void testGetAssertionFunctions() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		assertEquals(6, convention.getAssertionFunctions().size());
	}

	@Test
	public void testDescribeFunctionBind() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		Node n = new Node();
		assertNull(convention.describeFunctionBind(n));
	}

	@Test
	public void testSafeNext() {
		ClosureCodingConvention convention = new ClosureCodingConvention();
		Node n = new Node();
		assertNull(convention.safeNext(n));
	}

}