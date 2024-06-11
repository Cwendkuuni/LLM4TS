import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.node.NodeFactory.createString;

import com.google.common.base.Predicate;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.TestCases;
import com.google.javascript.rhino.testing.TopLevelTestingEnvironment;

import java.util.List;

/**
 * Test file for NamedType class.
 */
public class NamedTypeTest {

  private static final String SOURCE_NAME = "NamedTypeTest";
  private static final int LINE_NUMBER = 1;
  private static final int CHAR_NUMBER = 1;

  @TestCases.Test(expected = AssertionError.class)
  public void testNullReference() {
    JSTypeRegistry registry = new JSTypeRegistry();
    try {
      new NamedType(registry, null, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @TestCases.Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.define(createString("StringType"), StringType.INSTANCE);

    NamedType namedType = new NamedType(registry, "StringType", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);
    JSType referencedType = namedType.resolve(new ErrorReporter(), null);

    assertEquals(StringType.INSTANCE, referencedType);
  }

  @TestCases.Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.define(createString("ObjectType"), ObjectType.INSTANCE);

    ObjectType objectType = new ObjectType(registry, null);
    objectType.defineProperty(createString("prop"), StringType.INSTANCE, false, null);

    NamedType namedType = new NamedType(registry, "ObjectType.prop", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);
    JSType referencedType = namedType.resolve(new ErrorReporter(), null);

    assertEquals(StringType.INSTANCE, referencedType);
  }

  @TestCases.Test
  public void testResolveViaRegistryWithUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.define(createString("StringType"), StringType.INSTANCE);

    NamedType namedType = new NamedType(registry, "UnknownType", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);
    JSType referencedType = namedType.resolve(new ErrorReporter(), null);

    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE),
        referencedType);
  }

  @TestCases.Test
  public void testResolveViaPropertiesWithUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.define(createString("ObjectType"), ObjectType.INSTANCE);

    ObjectType objectType = new ObjectType(registry, null);
    objectType.defineProperty(createString("prop"), null, false, null);

    NamedType namedType = new NamedType(registry, "ObjectType.prop", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);
    JSType referencedType = namedType.resolve(new ErrorReporter(), null);

    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE),
        referencedType);
  }

  @TestCases.Test
  public void testResolveViaPropertiesWithInvalidType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.define(createString("ObjectType"), ObjectType.INSTANCE);

    ObjectType objectType = new ObjectType(registry, null);
    objectType.defineProperty(createString("prop"), BooleanType.INSTANCE, false, null);

    NamedType namedType = new NamedType(registry, "ObjectType.prop", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);
    JSType referencedType = namedType.resolve(new ErrorReporter(), null);

    assertEquals(StringType.INSTANCE, referencedType);
  }

  @TestCases.Test
  public void testResolveViaPropertiesWithUnknownTypeInFirstComponent() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.define(createString("UnknownType.StringType"), StringType.INSTANCE);

    ObjectType objectType = new ObjectType(registry, null);
    objectType.defineProperty(createString("prop"), null, false, null);

    NamedType namedType = new NamedType(registry, "UnknownType.StringType.prop",
        SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    JSType referencedType = namedType.resolve(new ErrorReporter(), null);

    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE),
        referencedType);
  }

  @TestCases.Test
  public void testResolveViaPropertiesWithUnknownTypeInMiddleComponent() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.define(createString("ObjectType1.ObjectType2.StringType"),
        StringType.INSTANCE);

    ObjectType objectType1 = new ObjectType(registry, null);
    ObjectType objectType2 = new ObjectType(registry, null);
    objectType1.defineProperty(createString("prop1"), objectType2, false, null);

    NamedType namedType = new NamedType(registry, "ObjectType1.ObjectType2.prop1",
        SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    JSType referencedType = namedType.resolve(new ErrorReporter(), null);

    assertEquals(StringType.INSTANCE, referencedType);
  }

  @TestCases.Test
  public void testResolveViaPropertiesWithLoop() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.define(createString("LoopType.property"), LoopType.INSTANCE);

    ObjectType objectType = new ObjectType(registry, null);
    objectType.defineProperty(createString("loopProperty"), null, false, null);

    NamedType namedType = new NamedType(registry, "LoopType.property.loopProperty",
        SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    try {
      namedType.resolve(new ErrorReporter(), null);
      fail();
    } catch (AssertionError e) {
      // expected
    }
  }

  @TestCases.Test
  public void testResolveViaPropertiesWithLoopForEnum() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.define(createString("EnumType.property"), EnumType.INSTANCE);

    ObjectType objectType = new ObjectType(registry, null);
    objectType.defineProperty(createString("property"), null, false, null);

    NamedType namedType = new NamedType(registry, "EnumType.property.property",
        SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    try {
      namedType.resolve(new ErrorReporter(), null);
      fail();
    } catch (AssertionError e) {
      // expected
    }
  }

  @TestCases.Test
  public void testIsNamedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "StringType", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);

    assertTrue(namedType.isNamedType());
  }

  @TestCases.Test
  public void testIsNominalType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "StringType", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);

    assertTrue(namedType.isNominalType());
  }

  @TestCases.Test
  public void testIsResolved() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "StringType", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);

    assertFalse(namedType.isResolved());
  }

  @TestCases.Test
  public void testIsResolvedAfterResolve() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.define(createString("StringType"), StringType.INSTANCE);

    NamedType namedType = new NamedType(registry, "StringType", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);
    namedType.resolve(new ErrorReporter(), null);

    assertTrue(namedType.isResolved());
  }

  @TestCases.Test
  public void testGetReferenceName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "StringType", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);

    assertEquals("StringType", namedType.getReferenceName());
  }

  @TestCases.Test
  public void testGetReferencedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.define(createString("StringType"), StringType.INSTANCE);

    NamedType namedType = new NamedType(registry, "StringType", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);
    namedType.resolve(new ErrorReporter(), null);

    assertEquals(StringType.INSTANCE, namedType.getReferencedType());
  }

  @TestCases.Test
  public void testGetReferencedTypeBeforeResolve() {
    JSTypeRegistry registry = new JSTypeRegistry();

    NamedType namedType = new NamedType(registry, "StringType", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);

    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
        namedType.getReferencedType());
  }

  @TestCases.Test
  public void testToStringHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "StringType", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);

    assertEquals("StringType", namedType.toStringHelper(false));
  }

  @TestCases.Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "StringType", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);

    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof StringType;
      }
    };

    namedType.setValidator(validator);

    JSType referencedType = namedType.resolve(new ErrorReporter(), null);

    assertTrue(validator.apply(referencedType));
  }

  @TestCases.Test
  public void testSetValidatorWithNullValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "StringType", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);

    namedType.setValidator(null);

    JSType referencedType = namedType.resolve(new ErrorReporter(), null);

    assertFalse(referencedType instanceof StringType);
  }

  @TestCases.Test
  public void testSetValidatorWithInvalidValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "StringType", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);

    Predicate<Integer> invalidValidator = new Predicate<Integer>() {
      @Override
      public boolean apply(Integer input) {
        return input instanceof Integer;
      }
    };

    try {
      namedType.setValidator(invalidValidator);
      fail();
    } catch (ClassCastException e) {
      // expected
    }
  }

  @TestCases.Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType1 = new NamedType(registry, "StringType1", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);
    NamedType namedType2 = new NamedType(registry, "StringType2", SOURCE_NAME,
        LINE_NUMBER, CHAR_NUMBER);

    assertEquals(namedType1.hashCode(), namedType1.hashCode());
    assertEquals(namedType1.hashCode(), namedType1.hashCode());
    assertNotEquals(namedType1.hashCode(), namedType2.hashCode());
  }
}