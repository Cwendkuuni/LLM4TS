import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType[] expected = new NamedType[]{new NamedType(String.class), new NamedType(Integer.class)};
        subtypeResolver.registerSubtypes(expected);
        Collection<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByClass(null, null);
        assertEquals(Arrays.asList(expected), actual);
    }

    @Test
    public void testRegisterSubtypeClasses() {
        Class<?>[] classes = new Class<?>[]{String.class, Integer.class};
        NamedType[] expected = new NamedType[]{new NamedType(String.class), new NamedType(Integer.class)};
        subtypeResolver.registerSubtypes(classes);
        Collection<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByClass(null, null);
        assertEquals(Arrays.asList(expected), actual);
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithConfig() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        List<NamedType> expected = new ArrayList<NamedType>();
        Collection<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithType() {
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        List<NamedType> expected = new ArrayList<NamedType>();
        Collection<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByClass(config, type);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithConfig() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        List<NamedType> expected = new ArrayList<NamedType>();
        Collection<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithType() {
        MapperConfig<?> config = null;
        AnnotatedClass baseType = null;
        List<NamedType> expected = new ArrayList<NamedType>();
        Collection<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseType);
        assertEquals(expected, actual);
    }
}