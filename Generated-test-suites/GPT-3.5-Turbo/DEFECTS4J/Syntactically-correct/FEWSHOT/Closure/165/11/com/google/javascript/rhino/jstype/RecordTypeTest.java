package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class RecordTypeTest {

	private JSTypeRegistry registry;
	private RecordType testRecordType;

	@Before
	public void setUp() {
		registry = new JSTypeRegistry();
		testRecordType = new RecordType(registry, new TreeMap<String, RecordProperty>());
	}

	@Test
	public void testIsEquivalentTo() {
		assertTrue(testRecordType.isEquivalentTo(testRecordType));
		assertFalse(testRecordType.isEquivalentTo(null));
		assertFalse(testRecordType.isEquivalentTo(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE)));

		RecordType equivalentRecordType = new RecordType(registry, new TreeMap<String, RecordProperty>());
		assertTrue(testRecordType.isEquivalentTo(equivalentRecordType));

		RecordType differentRecordType = new RecordType(registry, new TreeMap<String, RecordProperty>());
		differentRecordType.properties.put("property1", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));
		assertFalse(testRecordType.isEquivalentTo(differentRecordType));

		RecordType differentPropertyRecordType = new RecordType(registry, new TreeMap<String, RecordProperty>());
		testRecordType.properties.put("property1", registry.getNativeType(JSTypeNative.STRING_TYPE));
		differentPropertyRecordType.properties.put("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
		assertFalse(testRecordType.isEquivalentTo(differentPropertyRecordType));
	}

	@Test
	public void testGetImplicitPrototype() {
		assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), testRecordType.getImplicitPrototype());
	}

	@Test
	public void testDefineProperty() {
		assertTrue(testRecordType.defineProperty("property", registry.getNativeType(JSTypeNative.STRING_TYPE),
				false, null));
		assertTrue(testRecordType.properties.containsKey("property"));
	}

	@Test
	public void testGetGreatestSubtypeHelper() {
		JSType otherType = registry.getNativeType(JSTypeNative.LINEAR_OBJECT_TYPE);
		assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE),
				testRecordType.getGreatestSubtypeHelper(otherType));
	}

	@Test
	public void testIsSubtype() {
		assertFalse(testRecordType.isSubtype(null));
		assertTrue(testRecordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

		RecordType subType = new RecordType(registry, new TreeMap<String, RecordProperty>());
		subType.properties.put("property", registry.getNativeType(JSTypeNative.STRING_TYPE));
		assertFalse(testRecordType.isSubtype(subType));

		RecordType superType = new RecordType(registry, new TreeMap<String, RecordProperty>());
		superType.properties.put("property", registry.getNativeType(JSTypeNative.ANY_TYPE));
		assertFalse(superType.isSubtype(testRecordType));

		superType.properties.put("property", registry.getNativeType(JSTypeNative.STRING_TYPE));
		assertTrue(superType.isSubtype(testRecordType));
	}

	@Test
	public void testResolveInternal() {
		ErrorReporter errorReporter = new ErrorReporter() {
			@Override
			public void warning(String message, String sourceName, int line, int lineOffset) { }

			@Override
			public void error(String message, String sourceName, int line, int lineOffset) { }

			@Override
			public void reportWarning(String message, String sourceName, int line, int lineOffset) { }

			@Override
			public void reportError(String message, String sourceName, int line, int lineOffset) { }

			@Override
			public String toString() {
				return "";
			}
		};

		StaticScope<JSType> scope = new StaticScope<>();
		JSType resolvedType = testRecordType.resolveInternal(errorReporter, scope);
		assertEquals(testRecordType, resolvedType);
	}

}