import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties = new TreeMap<>();
    properties.put("prop1", new RecordProperty("prop1", new IntType(registry)));
    properties.put("prop2", new RecordProperty("prop2", new StringType(registry)));

    RecordProperty prop1 = properties.get("prop1");
    RecordProperty prop2 = properties.get("prop2");

    RecordType recordType = new RecordType(registry, properties);

    assertEquals("RecordType", recordType.getName());
    assertEquals(2, recordType.getPropertyNames().size());

    assertNotNull(recordType.getPropertyType("prop1"));
    assertEquals(new IntType(registry), recordType.getPropertyType("prop1"));

    assertNotNull(recordType.getPropertyType("prop2"));
    assertEquals(new StringType(registry), recordType.getPropertyType("prop2"));
  }

  @Test
  public void testEquivalentTo() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties1 = new TreeMap<>();
    properties1.put("prop1", new RecordProperty("prop1", new IntType(registry)));

    Map<String, RecordProperty> properties2 = new TreeMap<>();
    properties2.put("prop1", new RecordProperty("prop1", new IntType(registry)));
    properties2.put("prop2", new RecordProperty("prop2", new StringType(registry)));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testNotEquivalentToDifferentNumberOfProperties() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties1 = new TreeMap<>();
    properties1.put("prop1", new RecordProperty("prop1", new IntType(registry)));

    Map<String, RecordProperty> properties2 = new TreeMap<>();
    properties2.put("prop1", new RecordProperty("prop1", new IntType(registry)));
    properties2.put("prop2", new RecordProperty("prop2", new StringType(registry)));
    properties2.put("prop3", new RecordProperty("prop3", new IntType(registry)));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testNotEquivalentToDifferentPropertyTypes() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties1 = new TreeMap<>();
    properties1.put("prop1", new RecordProperty("prop1", new IntType(registry)));

    Map<String, RecordProperty> properties2 = new TreeMap<>();
    properties2.put("prop1", new RecordProperty("prop1", new StringType(registry)));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testDefinePropertyFrozen() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties = new TreeMap<>();
    properties.put("prop1", new RecordProperty("prop1", new IntType(registry)));

    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.defineProperty("prop1", new IntType(registry), false, null));
  }

  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    Map<String, RecordProperty> properties1 = new TreeMap<>();
    properties1.put("prop1", new RecordProperty("prop1", new IntType(registry)));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new TreeMap<>();
    properties2.put("prop1", new RecordProperty("prop1", new IntType(registry)));
    properties2.put("prop2", new RecordProperty("prop2", new StringType(registry)));
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType leastSupertype = recordType1.getLeastSupertype(recordType2);

    assertTrue(leastSupertype.isEquivalentTo(recordType2));
  }

  @Test
  public void testGetGreatestSubtypeHelper_IntObjType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    JSType intType = new IntType(registry);
    JSType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    RecordType recordType = new RecordType(registry, null);
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(objType);

    assertNull(greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeHelper_RecordType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties1 = new TreeMap<>();
    properties1.put("prop1", new RecordProperty("prop1", new IntType(registry)));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new TreeMap<>();
    properties2.put("prop1", new RecordProperty("prop1", new IntType(registry)));
    properties2.put("prop2", new RecordProperty("prop2", new StringType(registry)));
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertTrue(greatestSubtype.isEquivalentTo(recordType2));
  }

  @Test
  public void testGetGreatestSubtypeHelper_MismatchedTypes() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties1 = new TreeMap<>();
    properties1.put("prop1", new RecordProperty("prop1", new IntType(registry)));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new TreeMap<>();
    properties2.put("prop1", new RecordProperty("prop1", new StringType(registry)));
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertNull(greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeHelper_IncompatibleTypes() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties1 = new TreeMap<>();
    properties1.put("prop1", new RecordProperty("prop1", new IntType(registry)));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new TreeMap<>();
    properties2.put("prop1", new RecordProperty("prop1", new ObjectType(registry)));
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertNull(greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties1 = new TreeMap<>();
    properties1.put("prop1", new RecordProperty("prop1", new IntType(registry)));

    Map<String, RecordProperty> properties2 = new TreeMap<>();
    properties2.put("prop1", new RecordProperty("prop1", new IntType(registry)));
    properties2.put("prop2", new RecordProperty("prop2", new StringType(registry)));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeWithIncompatibleTypes() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties1 = new TreeMap<>();
    properties1.put("prop1", new RecordProperty("prop1", new IntType(registry)));

    Map<String, RecordProperty> properties2 = new TreeMap<>();
    properties2.put("prop1", new RecordProperty("prop1", new ObjectType(registry)));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeWithDifferentNumberOfProperties() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties1 = new TreeMap<>();
    properties1.put("prop1", new RecordProperty("prop1", new IntType(registry)));

    Map<String, RecordProperty> properties2 = new TreeMap<>();
    properties2.put("prop1", new RecordProperty("prop1", new IntType(registry)));
    properties2.put("prop2", new RecordProperty("prop2", new StringType(registry)));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties = new TreeMap<>();
    properties.put("prop1", new RecordProperty("prop1", new IntType(registry)));

    RecordType recordType = new RecordType(registry, properties);

    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> scope = Mockito.mock(StaticScope.class);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(Mockito.mock(ObjectType.class));

    when(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)).thenReturn(Mockito.mock(UnionType.class));

    when(registry.getNativeType(JSTypeNative.NO_TYPE)).thenReturn(Mockito.mock(VoidType.class));

    when(registry.getEachReferenceTypeWithProperty("prop1")).thenReturn(new Object[] { Mockito.mock(ObjectType.class) });

    recordType.resolveInternal(errorReporter, scope);

    // TODO: Add assertions to test the resolution of properties.
  }
}