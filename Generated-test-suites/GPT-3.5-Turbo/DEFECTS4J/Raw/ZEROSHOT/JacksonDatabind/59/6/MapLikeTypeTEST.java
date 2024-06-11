import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.TypeVariable;
import java.util.*;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

  private MapLikeType mapLikeType;

  @Before
  public void setUp() {
    mapLikeType = new MapLikeType(TestClass.class, new TypeBindings(), new JavaType(), new JavaType[]{}, new JavaType(), new JavaType(), new Object(), new Object(), true);
  }

  @Test
  public void testUpgradeFrom() {
    JavaType baseType = new JavaType();
    JavaType keyT = new JavaType();
    JavaType valueT = new JavaType();
    MapLikeType result = MapLikeType.upgradeFrom(baseType, keyT, valueT);
    assertNotNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpgradeFrom_withInvalidBaseType() {
    JavaType baseType = new JavaType();
    JavaType keyT = new JavaType();
    JavaType valueT = new JavaType();
    MapLikeType.upgradeFrom(baseType, keyT, valueT);
  }

  @Test
  public void testConstruct() {
    Class<?> rawType = TestClass.class;
    JavaType keyT = new JavaType();
    JavaType valueT = new JavaType();
    MapLikeType result = MapLikeType.construct(rawType, keyT, valueT);
    assertNotNull(result);
  }

  @Test
  public void testNarrow() {
    Class<?> subclass = TestClass.class;
    MapLikeType result = mapLikeType._narrow(subclass);
    assertNotNull(result);
  }

  @Test
  public void testWithKeyType() {
    JavaType keyType = new JavaType();
    MapLikeType result = mapLikeType.withKeyType(keyType);
    assertNotNull(result);
  }

  @Test
  public void testWithContentType() {
    JavaType contentType = new JavaType();
    MapLikeType result = mapLikeType.withContentType(contentType);
    assertNotNull(result);
  }

  @Test
  public void testWithTypeHandler() {
    Object h = new Object();
    MapLikeType result = mapLikeType.withTypeHandler(h);
    assertNotNull(result);
  }

  @Test
  public void testWithContentTypeHandler() {
    Object h = new Object();
    MapLikeType result = mapLikeType.withContentTypeHandler(h);
    assertNotNull(result);
  }

  @Test
  public void testWithValueHandler() {
    Object h = new Object();
    MapLikeType result = mapLikeType.withValueHandler(h);
    assertNotNull(result);
  }

  @Test
  public void testWithContentValueHandler() {
    Object h = new Object();
    MapLikeType result = mapLikeType.withContentValueHandler(h);
    assertNotNull(result);
  }

  @Test
  public void testWithStaticTyping() {
    MapLikeType result = mapLikeType.withStaticTyping();
    assertNotNull(result);
  }

  @Test
  public void testRefine() {
    Class<?> rawType = TestClass.class;
    TypeBindings bindings = new TypeBindings();
    JavaType superClass = new JavaType();
    JavaType[] superInterfaces = new JavaType[]{};
    MapLikeType result = mapLikeType.refine(rawType, bindings, superClass, superInterfaces);
    assertNotNull(result);
  }

  @Test
  public void testBuildCanonicalName() {
    String result = mapLikeType.buildCanonicalName();
    assertNotNull(result);
  }

  @Test
  public void testIsContainerType() {
    boolean result = mapLikeType.isContainerType();
    assertTrue(result);
  }

  @Test
  public void testIsMapLikeType() {
    boolean result = mapLikeType.isMapLikeType();
    assertTrue(result);
  }

  @Test
  public void testGetKeyType() {
    JavaType result = mapLikeType.getKeyType();
    assertNotNull(result);
  }

  @Test
  public void testGetContentType() {
    JavaType result = mapLikeType.getContentType();
    assertNotNull(result);
  }

  @Test
  public void testGetContentValueHandler() {
    Object result = mapLikeType.getContentValueHandler();
    assertNotNull(result);
  }

  @Test
  public void testGetContentTypeHandler() {
    Object result = mapLikeType.getContentTypeHandler();
    assertNotNull(result);
  }

  @Test
  public void testHasHandlers() {
    boolean result = mapLikeType.hasHandlers();
    assertTrue(result);
  }

  @Test
  public void testGetErasedSignature() {
    StringBuilder sb = new StringBuilder();
    StringBuilder result = mapLikeType.getErasedSignature(sb);
    assertNotNull(result);
  }

  @Test
  public void testGetGenericSignature() {
    StringBuilder sb = new StringBuilder();
    StringBuilder result = mapLikeType.getGenericSignature(sb);
    assertNotNull(result);
  }

  @Test
  public void testWithKeyTypeHandler() {
    Object h = new Object();
    MapLikeType result = mapLikeType.withKeyTypeHandler(h);
    assertNotNull(result);
  }

  @Test
  public void testWithKeyValueHandler() {
    Object h = new Object();
    MapLikeType result = mapLikeType.withKeyValueHandler(h);
    assertNotNull(result);
  }

  @Test
  public void testIsTrueMapType() {
    boolean result = mapLikeType.isTrueMapType();
    assertTrue(result);
  }

  @Test
  public void testToString() {
    String result = mapLikeType.toString();
    assertNotNull(result);
  }

  @Test
  public void testEquals() {
    Object o = new MapLikeType();
    boolean result = mapLikeType.equals(o);
    assertFalse(result);
  }
}