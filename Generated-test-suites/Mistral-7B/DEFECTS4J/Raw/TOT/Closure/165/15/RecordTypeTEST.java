import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.testing.TypeEqualsTester;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class RecordTypeTest {

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType typeA = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType typeB = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .setProperty("a", registry.getNativeStringType());
    JSType typeC = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .setProperty("a", registry.getNativeNumberType())
        .setProperty("b", registry.getNativeStringType());

    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap());
    RecordType recordTypeB = new RecordType(registry, Maps.newHashMap()
        .put("a", new RecordProperty("a", registry.getNativeStringType())));
    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap()
        .put("a", new RecordProperty("a", registry.getNativeNumberType()))
        .put("b", new RecordProperty("b", registry.getNativeStringType())));

    TypeEqualsTester.getInstance()
        .typeEquals(typeA, recordTypeA)
        .and(typeA, typeB)
        .and(typeB, recordTypeB)
        .and(typeB, typeC)
        .and(typeB, recordTypeA) // should be true due to structural comparison
        .and(typeC, recordTypeC)
        .and(typeA, typeC) // should be false due to missing property "b"
        .and(typeC, typeB) // should be false due to different property types
        .assertAll();
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType typeA = new RecordType(registry, Maps.newHashMap()
        .put("a", registry.getNativeStringType())
        .put("b", registry.getNativeNumberType()));
    ObjectType typeB = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .setProperty("a", registry.getNativeStringType())
        .setProperty("b", registry.getNativeStringType());

    assertThat(typeA.isSubtype(typeB)).isTrue();
  }

  @Test
  public void testRecordTypeIsNotSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType typeA = new RecordType(registry, Maps.newHashMap()
        .put("a", registry.getNativeStringType())
        .put("b", registry.getNativeNumberType()));
    ObjectType typeB = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .setProperty("a", registry.getNativeNumberType())
        .setProperty("b", registry.getNativeStringType());

    assertThat(typeA.isSubtype(typeB)).isFalse();
  }
}