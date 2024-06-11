import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.util.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;


public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;
    private MapperConfig<?> mapperConfig;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
        mapperConfig = new MapperConfig<>();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = new NamedType[]{new NamedType(String.class), new NamedType(Integer.class)};
        subtypeResolver.registerSubtypes(types);

        Set<NamedType> registeredSubtypes = subtypeResolver._registeredSubtypes;
        Assert.assertEquals(2, registeredSubtypes.size());
        Set<NamedType> expectedSubtypes = new HashSet<>(Arrays.asList(types));
        Assert.assertEquals(expectedSubtypes, registeredSubtypes);
    }

    @Test
    public void testRegisterClassSubtypes() {
        Class<?>[] classs = new Class<?>[]{String.class, Integer.class};
        subtypeResolver.registerSubtypes(classs);

        Set<NamedType> registeredSubtypes = subtypeResolver._registeredSubtypes;
        Assert.assertEquals(2, registeredSubtypes.size());
        Set<NamedType> expectedSubtypes = new HashSet<>();
        for (Class<?> cls : classs) {
            expectedSubtypes.add(new NamedType(cls));
        }
        Assert.assertEquals(expectedSubtypes, registeredSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithProperty() {
        AnnotatedMember property = null;
        JavaType baseType = null;

        List<NamedType> expectedSubtypes = new ArrayList<>();

        AnnotationIntrospector ai = mapperConfig.getAnnotationIntrospector();
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(mapperConfig, property, baseType);
        Assert.assertEquals(expectedSubtypes.size(), result.size());
        Assert.assertTrue(result.containsAll(expectedSubtypes));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithType() {
        AnnotatedClass type = null;

        List<NamedType> expectedSubtypes = new ArrayList<>();

        AnnotationIntrospector ai = mapperConfig.getAnnotationIntrospector();
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(mapperConfig, type);
        Assert.assertEquals(expectedSubtypes.size(), result.size());
        Assert.assertTrue(result.containsAll(expectedSubtypes));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithProperty() {
        AnnotatedMember property = null;
        JavaType baseType = null;

        List<NamedType> expectedSubtypes = new ArrayList<>();

        AnnotationIntrospector ai = mapperConfig.getAnnotationIntrospector();
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(mapperConfig, property, baseType);
        Assert.assertEquals(expectedSubtypes.size(), result.size());
        Assert.assertTrue(result.containsAll(expectedSubtypes));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithType() {
        AnnotatedClass type = null;

        List<NamedType> expectedSubtypes = new ArrayList<>();

        AnnotationIntrospector ai = mapperConfig.getAnnotationIntrospector();
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(mapperConfig, type);
        Assert.assertEquals(expectedSubtypes.size(), result.size());
        Assert.assertTrue(result.containsAll(expectedSubtypes));
    }
}