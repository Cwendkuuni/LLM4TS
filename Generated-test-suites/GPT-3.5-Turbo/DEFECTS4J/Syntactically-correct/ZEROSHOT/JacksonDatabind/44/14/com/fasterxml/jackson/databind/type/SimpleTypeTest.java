import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;
import java.util.Collection;
import java.util.Map;

public class SimpleTypeTest {

  @Test
  public void testConstruct() {
    try {
      SimpleType.construct(Map.class);
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Map"));
    }
    try {
      SimpleType.construct(Collection.class);
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Collection"));
    }
    try {
      SimpleType.construct(int[].class);
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("array"));
    }
  }

  @Test
  public void testConstructUnsafe() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    assertNotNull(type);
  }

  @Test
  public void testWithContentType() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    try {
      type.withContentType(SimpleType.constructUnsafe(Integer.class));
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("no content types"));
    }
  }

  @Test
  public void testWithContentTypeHandler() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    try {
      type.withContentTypeHandler(new Object());
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("no content types"));
    }
  }

  @Test
  public void testWithContentValueHandler() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    try {
      type.withContentValueHandler(new Object());
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("no content types"));
    }
  }

  @Test
  public void testWithStaticTyping() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    SimpleType staticType = type.withStaticTyping();
    assertTrue(staticType.isStatic());
  }

  @Test
  public void testRefine() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    assertNull(type.refine(String.class, TypeBindings.emptyBindings(), null, null));
  }

  @Test
  public void testGetErasedSignature() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    StringBuilder sb = new StringBuilder();
    StringBuilder result = type.getErasedSignature(sb);
    assertEquals("Ljava/lang/String;", result.toString());
  }

  @Test
  public void testGetGenericSignature() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    StringBuilder sb = new StringBuilder();
    StringBuilder result = type.getGenericSignature(sb);
    assertEquals("Ljava/lang/String;;", result.toString());
  }

  @Test
  public void testToString() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    assertEquals("[simple type, class java.lang.String]", type.toString());
  }

  @Test
  public void testEquals() {
    SimpleType type1 = SimpleType.constructUnsafe(String.class);
    SimpleType type2 = SimpleType.constructUnsafe(String.class);
    assertTrue(type1.equals(type2));
    assertTrue(type2.equals(type1));
  }
}