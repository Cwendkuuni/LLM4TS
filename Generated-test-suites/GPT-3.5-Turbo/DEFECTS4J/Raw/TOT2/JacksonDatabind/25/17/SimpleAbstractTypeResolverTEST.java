import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import java.util.Collection;
import java.util.LinkedList;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test valid mapping
        resolver.addMapping(Collection.class, LinkedList.class);
        assertNotNull(resolver._mappings.get(new ClassKey(Collection.class)));
        
        // Test invalid mapping
        try {
            resolver.addMapping(LinkedList.class, Collection.class);
            fail("Should throw IllegalArgumentException for invalid mapping");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        JavaType result = resolver.findTypeMapping(null, new JavaType());
        assertNotNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType result = resolver.resolveAbstractType(null, new JavaType());
        assertNull(result);
    }

}