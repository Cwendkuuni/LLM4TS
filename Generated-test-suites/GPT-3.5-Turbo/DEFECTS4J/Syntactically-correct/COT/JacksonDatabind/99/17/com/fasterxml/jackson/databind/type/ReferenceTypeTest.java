import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

  @Test
  public void testConstructor() {
    Class<?> cls = Class.class;
    TypeBindings bindings = TypeBindings.emptyBindings();
    JavaType superClass = null;
    JavaType[] superInterfaces = null;
    JavaType refType = null;
    JavaType anchorType = null;
    Object valueHandler = null;
    Object typeHandler = null;
    boolean asStatic = false;

    ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInterfaces, refType, anchorType, valueHandler, typeHandler, asStatic);

    Assert.assertEquals(cls, referenceType._class);
    Assert.assertEquals(bindings, referenceType._bindings);
    Assert.assertEquals(superClass, referenceType._superClass);
    Assert.assertEquals(superInterfaces, referenceType._superInterfaces);
    Assert.assertEquals(refType, referenceType._referencedType);
    Assert.assertEquals(anchorType, referenceType._anchorType);
    Assert.assertEquals(valueHandler, referenceType._valueHandler);
    Assert.assertEquals(typeHandler, referenceType._typeHandler);
    Assert.assertEquals(asStatic, referenceType._asStatic);
  }

  @Test
  public void testConstructorWithUpgradeFrom() {
    JavaType baseType = null;
    JavaType refdType = null;

    ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);

    Assert.assertEquals(baseType, referenceType._referencedType);
    Assert.assertEquals(referenceType, referenceType._anchorType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithUpgradeFromAndMissingRefType() {
    JavaType baseType = null;
    JavaType refdType = null;

    ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithUpgradeFromAndInvalidBaseType() {
    JavaType baseType = null;
    JavaType refdType = null;
    JavaType invalidType = new JavaType();

    ReferenceType referenceType = ReferenceType.upgradeFrom(invalidType, refdType);
  }

  @Test
  public void testConstruct() {
    Class<?> cls = Class.class;
    TypeBindings bindings = TypeBindings.emptyBindings();
    JavaType superClass = null;
    JavaType[] superInterfaces = null;
    JavaType refType = null;

    ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInterfaces, refType);

    Assert.assertEquals(cls, referenceType._class);
    Assert.assertEquals(bindings, referenceType._bindings);
    Assert.assertEquals(superClass, referenceType._superClass);
    Assert.assertEquals(superInterfaces, referenceType._superInterfaces);
    Assert.assertEquals(refType, referenceType._referencedType);
    Assert.assertEquals(null, referenceType._anchorType);
    Assert.assertEquals(null, referenceType._valueHandler);
    Assert.assertEquals(null, referenceType._typeHandler);
    Assert.assertEquals(false, referenceType._asStatic);
  }

  @Test
  public void testConstructWithDeprecatedMethod() {
    Class<?> cls = Class.class;
    JavaType refType = null;

    ReferenceType referenceType = ReferenceType.construct(cls, refType);

    Assert.assertEquals(cls, referenceType._class);
    Assert.assertEquals(TypeBindings.emptyBindings(), referenceType._bindings);
    Assert.assertEquals(null, referenceType._superClass);
    Assert.assertEquals(null, referenceType._superInterfaces);
    Assert.assertEquals(null, referenceType._referencedType);
    Assert.assertEquals(refType, referenceType._anchorType);
    Assert.assertEquals(null, referenceType._valueHandler);
    Assert.assertEquals(null, referenceType._typeHandler);
    Assert.assertEquals(false, referenceType._asStatic);
  }

  @Test
  public void testWithContentType() {
    JavaType contentType = null;

    ReferenceType referenceType = new ReferenceType();

    JavaType newReferenceType = referenceType.withContentType(contentType);

    Assert.assertEquals(contentType, newReferenceType.getContentType());
  }

  @Test
  public void testWithTypeHandler() {
    Object h = null;

    ReferenceType referenceType = new ReferenceType();

    ReferenceType newReferenceType = referenceType.withTypeHandler(h);

    Assert.assertEquals(h, newReferenceType._typeHandler);
  }

  @Test
  public void testWithContentTypeHandler() {
    Object h = null;

    ReferenceType referenceType = new ReferenceType();

    ReferenceType newReferenceType = referenceType.withContentTypeHandler(h);

    Assert.assertEquals(h, newReferenceType._referencedType.getTypeHandler());
  }

  @Test
  public void testWithStaticTyping() {
    ReferenceType referenceType = new ReferenceType();

    ReferenceType newReferenceType = referenceType.withStaticTyping();

    Assert.assertEquals(true, newReferenceType._asStatic);
  }

  @Test
  public void testRefine() {
    Class<?> rawType = Class.class;
    TypeBindings bindings = null;
    JavaType superClass = null;
    JavaType[] superInterfaces = null;

    ReferenceType referenceType = new ReferenceType();

    JavaType newReferenceType = referenceType.refine(rawType, bindings, superClass, superInterfaces);

    Assert.assertEquals(rawType, newReferenceType._class);
    Assert.assertEquals(bindings, newReferenceType._bindings);
    Assert.assertEquals(superClass, newReferenceType._superClass);
    Assert.assertEquals(superInterfaces, newReferenceType._superInterfaces);
    Assert.assertEquals(null, newReferenceType._referencedType);
  }

  @Test
  public void testGetContentType() {
    ReferenceType referenceType = new ReferenceType();

    JavaType contentType = referenceType.getContentType();

    Assert.assertEquals(null, contentType);
  }

  @Test
  public void testGetReferencedType() {
    ReferenceType referenceType = new ReferenceType();

    JavaType referencedType = referenceType.getReferencedType();

    Assert.assertEquals(null, referencedType);
  }

  @Test
  public void testHasContentType() {
    ReferenceType referenceType = new ReferenceType();

    boolean hasContentType = referenceType.hasContentType();

    Assert.assertEquals(true, hasContentType);
  }

  @Test
  public void testIsReferenceType() {
    ReferenceType referenceType = new ReferenceType();

    boolean isReferenceType = referenceType.isReferenceType();

    Assert.assertEquals(true, isReferenceType);
  }

  @Test
  public void testGetAnchorType() {
    ReferenceType referenceType = new ReferenceType();

    JavaType anchorType = referenceType.getAnchorType();

    Assert.assertEquals(null, anchorType);
  }

  @Test
  public void testIsAnchorType() {
    ReferenceType referenceType = new ReferenceType();

    boolean isAnchorType = referenceType.isAnchorType();

    Assert.assertEquals(true, isAnchorType);
  }

  @Test
  public void testToString() {
    ReferenceType referenceType = new ReferenceType();

    String stringValue = referenceType.toString();

    Assert.assertNotNull(stringValue);
  }

  @Test
  public void testEquals() {
    ReferenceType referenceType = new ReferenceType();
    ReferenceType sameReferenceType = referenceType;
    ReferenceType equalReferenceType = new ReferenceType();
    equalReferenceType._class = referenceType._class;
    ReferenceType differentReferenceType = new ReferenceType();
    differentReferenceType._class = Integer.class;

    Assert.assertTrue(referenceType.equals(sameReferenceType));
    Assert.assertTrue(referenceType.equals(equalReferenceType));
    Assert.assertFalse(referenceType.equals(differentReferenceType));
    Assert.assertFalse(referenceType.equals(null));
    Assert.assertFalse(referenceType.equals(new String()));
  }
}