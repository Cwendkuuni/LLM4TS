package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.SourcePosition.UNKNOWN;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.RecordType;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RecordTypeTest {
   
    @Mock
    private ErrorReporter errorReporter;
   
    @Mock
    private StaticScope<JSType> scope;
   
    private JSTypeRegistry registry;
   
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        registry = new JSTypeRegistry();
    }
   
    @Test
    public void testRecordType() {
        RecordType recordType = new RecordType(registry, ImmutableMap.of("a", new RecordTypeBuilder.RecordProperty(null, null)));
        assertFalse(recordType.isFrozen());
        assertTrue(recordType.isDefined());
        assertEquals("a", recordType.getProperties().keySet().iterator().next());
    }
  
    @Test
    public void testGreatestSubtypeHelper() {
        RecordType recordType1 = new RecordType(registry, ImmutableMap.of("a", new RecordTypeBuilder.RecordProperty(null, null)));
        RecordType recordType2 = new RecordType(registry, ImmutableMap.of("b", new RecordTypeBuilder.RecordProperty(null, null)));
        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertFalse(greatestSubtype.isDefined());
    }
  
    @Test
    public void testIsEquivalentTo() {
        RecordType recordType1 = new RecordType(registry, ImmutableMap.of("a", new RecordTypeBuilder.RecordProperty(null, null)));
        RecordType recordType2 = new RecordType(registry, ImmutableMap.of("a", new RecordTypeBuilder.RecordProperty(null, null)));
        assertTrue(recordType1.isEquivalentTo(recordType2));
    }
  
    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType(registry, ImmutableMap.of("a", new RecordTypeBuilder.RecordProperty(null, null)));
        RecordType recordType2 = new RecordType(registry, ImmutableMap.of("b", new RecordTypeBuilder.RecordProperty(null, null)));
        assertFalse(recordType1.isSubtype(recordType2));
    }
  
    @Test
    public void testIsSubtypeWithObjectType() {
        ObjectType objectType = registry.getObjectType();
        RecordType recordType = new RecordType(registry, ImmutableMap.of("a", new RecordTypeBuilder.RecordProperty(null, null)));
        assertTrue(recordType.isSubtype(objectType));
    }
  
    @Test
    public void testResolveInternal() {
        Node node = new Node(Token.SCRIPT);
        SourcePosition sourcePosition = new SourcePosition(0, 0, 0);
        node.setStaticSourceFile(sourcePosition, UNKNOWN, null);
        RecordType recordType = new RecordType(registry, ImmutableMap.of("a", new RecordTypeBuilder.RecordProperty(node, null)));
        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        assertEquals(recordType, resolvedType);
    }
  
    @Test
    public void testIsSubtypeWithRecordType() {
        RecordType recordType1 = new RecordType(registry, ImmutableMap.of("a", new RecordTypeBuilder.RecordProperty(null, null)));
        RecordType recordType2 = new RecordType(registry, ImmutableMap.of("b", new RecordTypeBuilder.RecordProperty(null, null)));
        assertFalse(RecordType.isSubtype(recordType1, recordType2));
    }
}