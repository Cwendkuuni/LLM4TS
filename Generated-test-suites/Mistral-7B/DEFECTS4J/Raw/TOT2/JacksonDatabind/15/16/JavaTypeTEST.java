import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class JavaTypeTest {

    // Test method for constructor with valid arguments
    @Test
    public void testJavaTypeConstructor() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intClass);
        assertNotNull(javaType);
        assertEquals(Integer.class, javaType.getRawClass());
    }

    // Test method for isContainerType() method
    @Test
    public void testIsContainerType() {
        Class<List<Integer>> listClass = List.class.getTypeParameters()[0].getRawType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
        assertTrue(javaType.isContainerType());
    }

    // Test method for isMapLikeType() method
    @Test
    public void testIsMapLikeType() {
        Class<Map<String, Integer>> mapClass = Map.class.getTypeParameters()[0].getType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(mapClass);
        assertTrue(javaType.isMapLikeType());
    }

    // Test method for narrowBy() method with valid argument
    @Test
    public void testNarrowBy() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType narrowedType = javaType.narrowBy(Integer[].class);
        assertNotNull(narrowedType);
        assertTrue(Integer[].class.isAssignableFrom(narrowedType.getRawClass()));
    }

    // Test method for widenBy() method with valid argument
    @Test
    public void testWidenBy() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType widenedType = javaType.widenBy(Number.class);
        assertNotNull(widenedType);
        assertTrue(Number.class.isAssignableFrom(widenedType.getRawClass()));
    }

    // Test method for isInterface() method
    @Test
    public void testIsInterface() {
        Class<List<Integer>> listClass = List.class.getTypeParameters()[0].getType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
        assertTrue(javaType.isInterface());
    }

    
}