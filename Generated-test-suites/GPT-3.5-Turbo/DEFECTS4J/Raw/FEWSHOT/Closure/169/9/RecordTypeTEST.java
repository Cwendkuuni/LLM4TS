package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

	private JSTypeRegistry registry;
	private RecordType recordType;
	private RecordType emptyRecordType;
	private RecordType referencedRecordType;
	private RecordType inferredRecordType;
	
	@Before
	public void setUp() {
		registry = new JSTypeRegistry();
		recordType = new RecordType(registry, null);
		emptyRecordType = new RecordType(registry, null, false);
		referencedRecordType = new RecordType(registry, null, true);
		inferredRecordType = new RecordType(registry, null, true);
		
		recordType.defineDeclaredProperty("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
		recordType.defineDeclaredProperty("property2", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
		
		referencedRecordType.defineDeclaredProperty("property1", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null);
		referencedRecordType.defineDeclaredProperty("property2", registry.getNativeType(JSTypeNative.UNDEFINED_TYPE), null);
		
		inferredRecordType.defineSynthesizedProperty("property1", registry.getNativeType(JSTypeNative.NULL_OR_UNDEFIEND_TYPE), null);
		inferredRecordType.defineSynthesizedProperty("property2", registry.getNativeType(JSTypeNative.TOP_OBJECT_PROTO_TYPE), null);
	}
	
	@Test
	public void testIsSynthetic() {
		assertFalse(recordType.isSynthetic());
		assertFalse(emptyRecordType.isSynthetic());
		assertTrue(referencedRecordType.isSynthetic());
		assertTrue(inferredRecordType.isSynthetic());
	}
	
	@Test
	public void testCheckRecordEquivalenceHelper() {
		assertFalse(recordType.checkRecordEquivalenceHelper(referencedRecordType, true));
		assertFalse(referencedRecordType.checkRecordEquivalenceHelper(recordType, true));
		assertTrue(referencedRecordType.checkRecordEquivalenceHelper(referencedRecordType, true));
		assertTrue(recordType.checkRecordEquivalenceHelper(recordType, true));
	}
	
	@Test
	public void testIsSubtype() {
		assertFalse(recordType.isSubtype(emptyRecordType));
		assertFalse(referencedRecordType.isSubtype(emptyRecordType));
		assertTrue(emptyRecordType.isSubtype(recordType));
		assertTrue(emptyRecordType.isSubtype(referencedRecordType));
	}
	
	@Test
	public void testResolveInternal() {
		assertTrue(inferredRecordType.resolveInternal(null, null) instanceof RecordType);
	}
		
}