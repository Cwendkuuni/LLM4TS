import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

public class RecordTypeTest {

    private JSTypeRegistry registry;
    private RecordType recordType;
    private Map<String, RecordProperty> properties;

    @Before
    public void setUp() {
        registry = mock(JSTypeRegistry.class);
        properties = new HashMap<>();
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testConstructor() {
        assertNotNull(recordType);
        assertTrue(recordType.isPrettyPrint());
        assertFalse(recordType.isSynthetic());
        assertTrue(recordType.isFrozen());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_NullRecordProperty() {
        properties.put("property", null);
        new RecordType(registry, properties);
    }

    @Test
    public void testDefineProperty_FrozenRecordType() {
        recordType.setFrozen(true);
        assertFalse(recordType.defineProperty("property", mock(JSType.class), true, mock(Node.class)));
    }

    @Test
    public void testGetGreatestSubtypeHelper_RecordType() {
        JSType that = mock(JSType.class);
        RecordType thatRecord = mock(RecordType.class);
        RecordTypeBuilder builder = mock(RecordTypeBuilder.class);

        when(that.isRecordType()).thenReturn(true);
        when(that.toMaybeRecordType()).thenReturn(thatRecord);
        when(registry.getRecordTypeBuilder()).thenReturn(builder);
        when(builder.build()).thenReturn(thatRecord);

        assertFalse(recordType.getGreatestSubtypeHelper(that).isEmptyType());

        verify(thatRecord).hasProperty(anyString());
        verify(thatRecord).getPropertyType(anyString());
        verify(thatRecord).getPropertyNode(anyString());
        verify(builder).addProperty(anyString(), any(JSType.class), any(Node.class));
    }

    @Test
    public void testGetGreatestSubtypeHelper_NotRecordType() {
        JSType that = mock(JSType.class);
        ObjectType objectType = mock(ObjectType.class);

        when(that.isRecordType()).thenReturn(false);
        when(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)).thenReturn(objectType);

        assertEquals(objectType, recordType.getGreatestSubtypeHelper(that));

        verify(registry).getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    }

    @Test
    public void testToMaybeRecordType() {
        assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testIsSubtype_SubtypeHelper() {
        JSType that = mock(JSType.class);

        JSType.isSubtypeHelper(recordType, that);

        verify(registry).getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    }

    @Test
    public void testIsSubtype_RecordType() {
        JSType that = mock(JSType.class);
        RecordType thatRecord = mock(RecordType.class);

        when(that.isRecordType()).thenReturn(true);
        when(that.toMaybeRecordType()).thenReturn(thatRecord);
        when(thatRecord.getOwnPropertyNames()).thenReturn(properties.keySet());
        when(thatRecord.getPropertyType(anyString())).thenReturn(mock(JSType.class));

        assertFalse(RecordType.isSubtype(recordType, thatRecord));

        verify(thatRecord, atLeastOnce()).hasProperty(anyString());
        verify(thatRecord).getPropertyType(anyString());
    }

    @Test
    public void testIsSubtype_NotRecordType() {
        JSType that = mock(JSType.class);

        assertFalse(recordType.isSubtype(that));
    }

}