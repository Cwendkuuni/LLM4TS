import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdSubtypeResolverTest {

    private SubtypeResolver subtypeResolver;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MapperConfig config = new MapperConfig.StandardConfig();
        subtypeResolver = new StdSubtypeResolver();
        objectMapper = new ObjectMapper(config);
        objectMapper.setSubtypeResolver(subtypeResolver);
    }

    // Basic test case for registerSubtypes method
    @Test
    public void testRegisterSubtypes() {
        Class<?> baseType = String.class;
        Class<?> subType1 = Integer.class;
        Class<?> subType2 = Double.class;

        subtypeResolver.registerSubtypes(new NamedType[]{
                new NamedType(subType1),
                new NamedType(subType2)
        });

        JavaType baseJavaType = objectMapper.getTypeFactory().constructType(baseType);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseJavaType);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subType1)));
        assertTrue(subtypes.contains(new NamedType(subType2)));
    }

    // Test edge cases for registerSubtypesByClass method
    @Test
    public void testRegisterSubtypesEdgeCases() {
        Class<?> baseType = String.class;
        Class<?> subType1 = Integer.class;
        Class<?> subType2 = Double.class;
        Class<?> subType3 = Float.class;

        subtypeResolver.registerSubtypes(new NamedType[]{
                new NamedType(subType1),
                new NamedType(subType2)
        });

        JavaType baseJavaType = objectMapper.getTypeFactory().constructType(baseType);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseJavaType);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subType1)));
        assertTrue(subtypes.contains(new NamedType(subType2)));

        // Test for adding duplicate subtypes
        subtypeResolver.registerSubtypes(new NamedType[]{
                new NamedType(subType1),
                new NamedType(subType1)
        });

        JavaType baseJavaTypeDuplicate = objectMapper.getTypeFactory().constructType(baseType);
        List<NamedType> subtypesDuplicate = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseJavaTypeDuplicate);

        assertEquals(2, subtypesDuplicate.size());
        assertTrue(subtypesDuplicate.contains(new NamedType(subType1)));
        assertTrue(subtypesDuplicate.contains(new NamedType(subType1)));

        // Test for adding subtypes with the same name but different type
        subtypeResolver.registerSubtypes(new NamedType[]{
                new NamedType(subType1, "Integer"),
                new NamedType(subType2, "Integer")
        });

        JavaType baseJavaTypeWithSameName = objectMapper.getTypeFactory().constructType(baseType);
        List<NamedType> subtypesWithSameName = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseJavaTypeWithSameName);

        assertEquals(2, subtypesWithSameName.size());
        assertTrue(subtypesWithSameName.contains(new NamedType(subType1, "Integer")));
        assertTrue(subtypesWithSameName.contains(new NamedType(subType2, "Integer")));
    }

    // Test error scenarios for registerSubtypesByClass method
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterSubtypesErrorScenarios() {
        Class<?> baseType = String.class;

        // Test for passing null as argument
        subtypeResolver.registerSubtypes(null);

        // Test for passing an empty array
        subtypeResolver.registerSubtypes(new NamedType[]{});

        // Test for passing an array with a null element
        subtypeResolver.registerSubtypes(new NamedType[]{new NamedType(null)});

        // Test for passing an array with an element that is not an instance of NamedType
        subtypeResolver.registerSubtypes(new String[]{"NotANamedType"});
    }
}