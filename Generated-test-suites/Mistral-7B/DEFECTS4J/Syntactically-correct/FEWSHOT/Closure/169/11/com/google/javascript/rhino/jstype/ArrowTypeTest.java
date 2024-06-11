package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private ArrowTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setup() {
    registry = new ArrowTypeRegistry();
    errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, Object[] args) {}
    };
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = this.registry;
    ArrowType arrowType1 = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType2 = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE));

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));

    ArrowType arrowType3 = new ArrowType(registry, null, getNativeType(NUMBER));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType3.isSubtype(arrowType1));

    ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(STRING));
    assertFalse(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType4.isSubtype(arrowType1));

    ArrowType arrowType5 = new ArrowType(registry, null, getNativeType(BOOLEAN));
    assertFalse(arrowType1.isSubtype(arrowType5));
    assertFalse(arrowType5.isSubtype(arrowType1));

    ArrowType arrowType6 = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE), true);
    ArrowType arrowType7 = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE), true);

    assertTrue(arrowType6.isSubtype(arrowType7));
    assertTrue(arrowType7.isSubtype(arrowType6));

    ArrowType arrowType8 = new ArrowType(registry, null, getNativeType(NUMBER));
    ArrowType arrowType9 = new ArrowType(registry, null, getNativeType(NUMBER), true);

    assertTrue(arrowType8.isSubtype(arrowType9));
    assertTrue(arrowType9.isSubtype(arrowType8));

    ArrowType arrowType10 = new ArrowType(registry, null, getNativeType(STRING));
    ArrowType arrowType11 = new ArrowType(registry, null, getNativeType(STRING), true);

    assertTrue(arrowType10.isSubtype(arrowType11));
    assertTrue(arrowType11.isSubtype(arrowType10));

    ArrowType arrowType12 = new ArrowType(registry, null, getNativeType(BOOLEAN));
    ArrowType arrowType13 = new ArrowType(registry, null, getNativeType(BOOLEAN), true);

    assertTrue(arrowType12.isSubtype(arrowType13));
    assertTrue(arrowType13.isSubtype(arrowType12));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = this.registry;

    ArrowType arrowType1 =
        new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE), false);
    ArrowType arrowType2 =
        new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE), false);
    ArrowType arrowType3 = new ArrowType(registry, null, getNativeType(NUMBER));

    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));

    assertFalse(arrowType1.hasEqualParameters(arrowType3, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType3, true));

    ArrowType arrowType4 =
        new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE), true);
    ArrowType arrowType5 =
        new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE), true);

    assertTrue(arrowType4.hasEqualParameters(arrowType5, false));
    assertTrue(arrowType4.hasEqualParameters(arrowType5, true));

    ArrowType arrowType6 = new ArrowType(registry, null, getNativeType(NUMBER));
    ArrowType arrowType7 = new ArrowType(registry, null, getNativeType(NUMBER));
    ArrowType arrowType8 = new ArrowType(registry, null, getNativeType(NUMBER), true);

    assertTrue(arrowType6.hasEqualParameters(arrowType7, false));
    assertFalse(arrowType6.hasEqualParameters(arrowType7, true));
    assertTrue(arrowType6.hasEqualParameters(arrowType8, false));
    assertTrue(arrowType6.hasEqualParameters(arrowType8, true));

    ArrowType arrowType9 = new ArrowType(registry, null, getNativeType(STRING));
    ArrowType arrowType10 = new ArrowType(registry, null, getNativeType(STRING));
    ArrowType arrowType11 = new ArrowType(registry, null, getNativeType(STRING), true);

    assertTrue(arrowType9.hasEqualParameters(arrowType10, false));
    assertTrue(arrowType9.hasEqualParameters(arrowType10, true));
    assertTrue(arrowType9.hasEqualParameters(arrowType11, false));
    assertTrue(arrowType9.hasEqualParameters(arrowType11, true));

    ArrowType arrowType12 = new ArrowType(registry, null, getNativeType(BOOLEAN));
    ArrowType arrowType13 = new ArrowType(registry, null, getNativeType(BOOLEAN));
    ArrowType arrowType14 = new ArrowType(registry, null, getNativeType(BOOLEAN), true);

    assertTrue(arrowType12.hasEqualParameters(arrowType13, false));
    assertTrue(arrowType12.hasEqualParameters(arrowType13, true));
    assertTrue(arrowType12.hasEqualParameters(arrowType14, false));
    assertTrue(arrowType12.hasEqualParameters(arrowType14, true));

    ArrowType arrowType15 = new ArrowType(registry, null, null);
    ArrowType arrowType16 = new ArrowType(registry, null, null);
    ArrowType arrowType17 = new ArrowType(registry, null, getNativeType(NUMBER));

    assertTrue(arrowType15.hasEqualParameters(arrowType16, false));
    assertTrue(arrowType15.hasEqualParameters(arrowType16, true));
    assertFalse(arrowType15.hasEqualParameters(arrowType17, false));
    assertFalse(arrowType15.hasEqualParameters(arrowType17, true));
  }
}

class ArrowTypeRegistry extends JSTypeRegistry {
  @Override
  protected JSType newArrowType(Node arrowTypeNode, ArrowType arrowType) {
    return new ArrowType(this, arrowTypeNode, null, false);
  }
}