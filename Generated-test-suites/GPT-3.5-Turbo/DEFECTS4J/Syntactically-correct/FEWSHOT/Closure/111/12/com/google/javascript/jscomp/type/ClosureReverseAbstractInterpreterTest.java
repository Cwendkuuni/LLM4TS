package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
	
	private ClosureReverseAbstractInterpreter interpreter;
	
	@Before
	public void setUp() {
		interpreter = new ClosureReverseAbstractInterpreter(null, null);
	}
	
	@Test
	public void testGetPreciserScopeKnowingConditionOutcome() {
		FlowScope blindScope = new FlowScope();
		Node condition = new Node();
		boolean outcome = true;
		FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
		assertEquals(blindScope, result);
	}
	
	@Test
	public void testRestrictToArrayVisitor_caseTopType() {
		JSType type = new JSType();
		JSType result = interpreter.restrictToArrayVisitor.caseTopType(type);
		assertEquals(type, result);
	}
	
	@Test
	public void testRestrictToArrayVisitor_caseObjectType() {
		ObjectType type = new ObjectType();
		ObjectType arrayType = new ObjectType();
		JSType expected = arrayType;
		JSType result = interpreter.restrictToArrayVisitor.caseObjectType(type);
		assertEquals(expected, result);
	}
	
	@Test
	public void testRestrictToArrayVisitor_caseObjectType_notSubtype() {
		ObjectType type = new ObjectType();
		JSType arrayType = new JSType();
		JSType expected = null;
		JSType result = interpreter.restrictToArrayVisitor.caseObjectType(type);
		assertEquals(expected, result);
	}
	
	@Test
	public void testRestrictToNotArrayVisitor_caseObjectType() {
		ObjectType type = new ObjectType();
		JSType expected = null;
		JSType result = interpreter.restrictToNotArrayVisitor.caseObjectType(type);
		assertEquals(expected, result);
	}
	
	@Test
	public void testRestrictToNotArrayVisitor_caseObjectType_subtype() {
		ObjectType type = new ObjectType();
		JSType arrayType = new JSType();
		JSType expected = null;
		JSType result = interpreter.restrictToNotArrayVisitor.caseObjectType(type);
		assertEquals(expected, result);
	}
	
	@Test
	public void testRestrictToObjectVisitor_caseTopType() {
		JSType type = new JSType();
		JSType noObjectType = new JSType();
		JSType expected = noObjectType;
		JSType result = interpreter.restrictToObjectVisitor.caseTopType(type);
		assertEquals(expected, result);
	}
	
	@Test
	public void testRestrictToObjectVisitor_caseObjectType() {
		ObjectType type = new ObjectType();
		JSType expected = type;
		JSType result = interpreter.restrictToObjectVisitor.caseObjectType(type);
		assertEquals(expected, result);
	}
	
	@Test
	public void testRestrictToObjectVisitor_caseFunctionType() {
		FunctionType type = new FunctionType();
		JSType expected = type;
		JSType result = interpreter.restrictToObjectVisitor.caseFunctionType(type);
		assertEquals(expected, result);
	}
	
	@Test
	public void testRestrictToNotObjectVisitor_caseAllType() {
		JSType numberStringBooleanType = new JSType();
		JSType nullVoidType = new JSType();
		JSType expected = new JSType();
		JSType result = interpreter.restrictToNotObjectVisitor.caseAllType();
		assertEquals(expected, result);
	}
	
	@Test
	public void testRestrictToNotObjectVisitor_caseObjectType() {
		ObjectType type = new ObjectType();
		JSType expected = null;
		JSType result = interpreter.restrictToNotObjectVisitor.caseObjectType(type);
		assertEquals(expected, result);
	}
	
	@Test
	public void testRestrictToNotObjectVisitor_caseFunctionType() {
		FunctionType type = new FunctionType();
		JSType expected = null;
		JSType result = interpreter.restrictToNotObjectVisitor.caseFunctionType(type);
		assertEquals(expected, result);
	}
	
	@Test
	public void testClosureReverseAbstractInterpreter_constructor() {
		ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
	}
	
	@Test
	public void testRestrictParameter() {
		Node parameter = new Node();
		JSType type = new JSType();
		FlowScope blindScope = new FlowScope();
		Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
			@Override
			public JSType apply(TypeRestriction p) {
				return p.type;
			}
		};
		boolean outcome = true;
		FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
		assertEquals(blindScope, result);
	}
	
	@Test
	public void testTypeRestriction_constructor() {
		JSType type = new JSType();
		boolean outcome = true;
		ClosureReverseAbstractInterpreter.TypeRestriction typeRestriction = interpreter.new TypeRestriction(type, outcome);
		assertEquals(type, typeRestriction.type);
		assertEquals(outcome, typeRestriction.outcome);
	}

}