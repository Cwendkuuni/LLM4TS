import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import java.util.Collection;
import java.util.LinkedList;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        resolver.addMapping(Collection.class, LinkedList.class);
        JavaType type = resolver.findTypeMapping(DeserializationConfig.emptyInstance(), new JavaType());
        assertEquals(new LinkedList<>().getClass(), type.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameClass() {
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonSubType() {
        resolver.addMapping(Collection.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithConcreteSuperType() {
        resolver.addMapping(LinkedList.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        resolver.addMapping(Collection.class, LinkedList.class);
        JavaType type = resolver.findTypeMapping(DeserializationConfig.emptyInstance(), new JavaType(Collection.class));
        assertEquals(new LinkedList<>().getClass(), type.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        JavaType type = resolver.resolveAbstractType(DeserializationConfig.emptyInstance(), new JavaType());
        assertNull(type);
    }
}