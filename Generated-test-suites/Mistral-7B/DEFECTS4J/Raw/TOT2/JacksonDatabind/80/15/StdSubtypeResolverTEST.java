import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdSubtypeResolver;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdSubtypeResolverTest {

    private static final MapperFactory MAPPER_FACTORY = new MapperFactory();

    @Test
    public void testRegisterSubtypes() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // registered subtypes by array
        resolver.registerSubtypes(new Class<?>[]{ChildType1.class, ChildType2.class, ChildType3.class});
        // registered subtypes by NamedType array
        resolver.registerSubtypes(new NamedType[]{new NamedType(ChildType1.class), new NamedType(ChildType2.class), new NamedType(ChildType3.class)});
        // registered subtypes by collection
        resolver.registerSubtypes(Arrays.asList(ChildType1.class, ChildType2.class, ChildType3.class));
        // registered subtypes by NamedType collection
        resolver.registerSubtypes(Collections.singletonList(new NamedType(ChildType1.class)));
        // empty registration
        resolver.registerSubtypes();

        // check registration
        List<NamedType> registeredSubtypes = resolver.getRegisteredSubtypes();
        assertEquals(7, registeredSubtypes.size());
        assertEquals(ChildType1.class, registeredSubtypes.get(0).getType());
        assertEquals(ChildType2.class, registeredSubtypes.get(1).getType());
        assertEquals(ChildType3.class, registeredSubtypes.get(2).getType());
        assertEquals(ChildType1.class, registeredSubtypes.get(3).getType());
        assertEquals(ChildType2.class, registeredSubtypes.get(4).getType());
        assertEquals(ChildType3.class, registeredSubtypes.get(5).getType());
        assertNotNull(registeredSubtypes.get(6));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // register subtypes
        resolver.registerSubtypes(new Class<?>[]{ChildType1.class, ChildType2.class, ChildType3.class});

        // test with null baseType
        JavaType baseType = null;
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(MAPPER_FACTORY.getConfig(), null, baseType);
        assertEquals(3, subtypes.size());
        assertEquals(ChildType1.class, subtypes.get(0).getType());
        assertEquals(ChildType2.class, subtypes.get(1).getType());
        assertEquals(ChildType3.class, subtypes.get(2).getType());

        // test with String baseType
        baseType = MAPPER_FACTORY.constructType(String.class);
        subtypes = resolver.collectAndResolveSubtypesByClass(MAPPER_FACTORY.getConfig(), null, baseType);
        assertEquals(0, subtypes.size());

        // test with BaseType baseType
        baseType = MAPPER_FACTORY.constructType(BaseType.class);
        subtypes = resolver.collectAndResolveSubtypesByClass(MAPPER_FACTORY.getConfig(), null, baseType);
        assertEquals(3, subtypes.size());
        assertEquals(ChildType1.class, subtypes.get(0).getType());
        assertEquals(ChildType2.class, subtypes.get(1).getType());
        assertEquals(ChildType3.class, subtypes.get(2).getType());

        // test with ChildType1 baseType
        baseType = MAPPER_FACTORY.constructType(ChildType1.class);
        subtypes = resolver.collectAndResolveSubtypesByClass(MAPPER_FACTORY.getConfig(), null, baseType);
        assertEquals(2, subtypes.size());
        assertEquals(ChildType2.class, subtypes.get(0).getType());
        assertEquals(ChildType3.class, subtypes.get(1).getType());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // register subtypes
        resolver.registerSubtypes(new Class<?>[]{ChildType1.class, ChildType2.class, ChildType3.class});

        // test with null baseType
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(MAPPER_FACTORY.getConfig(), null, null);
        assertEquals(3, subtypes.size());
        assertEquals(ChildType1.class, subtypes.get(0).getType());
        assertEquals(ChildType2.class, subtypes.get(1).getType());
        assertEquals(ChildType3.class, subtypes.get(2).getType());

        // test with BaseType baseType
        JavaType baseType = MAPPER_FACTORY.constructType(BaseType.class);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(MAPPER_FACTORY.getConfig(), null, baseType);
        assertEquals(3, subtypes.size());
        assertEquals(ChildType1.class, subtypes.get(0).getType());
        assertEquals(ChildType2.class, subtypes.get(1).getType());
        assertEquals(ChildType3.class, subtypes.get(2).getType());

        // test with ChildType1 baseType
        JavaType childType1BaseType = MAPPER_FACTORY.constructType(ChildType1.class);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(MAPPER_FACTORY.getConfig(), null, childType1BaseType);
        assertEquals(2, subtypes.size());
        assertEquals(ChildType2.class, subtypes.get(0).getType());
        assertEquals(ChildType3.class, subtypes.get(1).getType());

        // test with ChildType1.Subtype1 baseType
        JavaType childType1Subtype1 = MAPPER_FACTORY.constructType(ChildType1_Subtype1.class);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(MAPPER_FACTORY.getConfig(), null, childType1Subtype1);
        assertEquals(1, subtypes.size());
        assertEquals(ChildType1_Subtype1.class, subtypes.get(0).getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdWithNullBaseType() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(MAPPER_FACTORY.getConfig(), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdWithEmptyBaseType() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType emptyBaseType = MAPPER_FACTORY.constructType(Object.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(MAPPER_FACTORY.getConfig(), null, emptyBaseType);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithExplicitTypeRegistration() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // register subtypes
        resolver.registerSubtypes(new Class<?>[]{ChildType1.class, ChildType2.class, ChildType3.class});
        // register an explicit type
        resolver.registerSubtypes(new NamedType(ChildTypeX.class));

        JavaType baseType = MAPPER_FACTORY.constructType(BaseType.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(MAPPER_FACTORY.getConfig(), null, baseType);
        assertEquals(4, subtypes.size());
        assertEquals(ChildType1.class, subtypes.get(0).getType());
        assertEquals(ChildType2.class, subtypes.get(1).getType());
        assertEquals(ChildType3.class, subtypes.get(2).getType());
        assertEquals(ChildTypeX.class, subtypes.get(3).getType());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithExplicitTypeRegistrationAndOverridden() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // register subtypes
        resolver.registerSubtypes(new Class<?>[]{ChildType1.class, ChildType2.class, ChildType3.class});
        // register an explicit type
        resolver.registerSubtypes(new NamedType(ChildTypeX.class));
        // register a subtype of ChildTypeX
        resolver.registerSubtypes(new Class<?>[]{ChildTypeX_Subtype1.class});

        JavaType baseType = MAPPER_FACTORY.constructType(BaseType.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(MAPPER_FACTORY.getConfig(), null, baseType);
        assertEquals(5, subtypes.size());
        assertEquals(ChildType1.class, subtypes.get(0).getType());
        assertEquals(ChildType2.class, subtypes.get(1).getType());
        assertEquals(ChildType3.class, subtypes.get(2).getType());
        assertEquals(ChildTypeX.class, subtypes.get(3).getType());
        assertEquals(ChildTypeX_Subtype1.class, subtypes.get(4).getType());
    }

    private static class BaseType {
    }

    private static class ChildType1 extends BaseType {
    }

    private static class ChildType1_Subtype1 extends ChildType1 {
    }

    private static class ChildType2 extends BaseType {
    }

    private static class ChildType3 extends BaseType {
    }

    private static class ChildTypeX extends BaseType {
    }

    private static class ChildTypeX_Subtype1 extends ChildTypeX {
    }
}