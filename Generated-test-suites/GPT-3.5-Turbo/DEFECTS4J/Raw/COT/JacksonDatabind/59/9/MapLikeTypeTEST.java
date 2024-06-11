import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {
  @Test
  public void testGetTypeParameters() {
    MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, JavaType.INT_TYPE, JavaType.STRING_TYPE, null, null, false);
    TypeVariable<?>[] vars = mapLikeType.getInstance().getTypeParameters();
    assertEquals(0, vars.length);
  }

  @Test
  public void testGetKeyType() {
    MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, JavaType.INT_TYPE, JavaType.STRING_TYPE, null, null, false);
    JavaType keyType = mapLikeType.getKeyType();
    assertEquals(JavaType.INT_TYPE, keyType);
  }

  @Test
  public void testGetContentType() {
    MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, JavaType.INT_TYPE, JavaType.STRING_TYPE, null, null, false);
    JavaType contentType = mapLikeType.getContentType();
    assertEquals(JavaType.STRING_TYPE, contentType);
  }

  @Test
  public void testHasHandlers() {
    MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, JavaType.INT_TYPE, JavaType.STRING_TYPE, null, null, false);
    assertTrue(mapLikeType.hasHandlers());
  }

  @Test
  public void testIsContainerType() {
    MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, JavaType.INT_TYPE, JavaType.STRING_TYPE, null, null, false);
    assertTrue(mapLikeType.isContainerType());
  }

  @Test
  public void testIsMapLikeType() {
    MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, JavaType.INT_TYPE, JavaType.STRING_TYPE, null, null, false);
    assertTrue(mapLikeType.isMapLikeType());
  }

  @Test
  public void testIsTrueMapType() {
    MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, JavaType.INT_TYPE, JavaType.STRING_TYPE, null, null, false);
    assertTrue(mapLikeType.isTrueMapType());
  }

  @Test
  public void testGetCanonicalName() {
    MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, JavaType.INT_TYPE, JavaType.STRING_TYPE, null, null, false);
    String canonicalName = mapLikeType.buildCanonicalName();
    assertEquals("java.util.Map<int,java.lang.String>", canonicalName);
  }

}