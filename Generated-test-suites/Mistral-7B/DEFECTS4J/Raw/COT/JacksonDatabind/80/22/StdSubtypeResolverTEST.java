import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig.Builder;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StdSubtypeResolverTest {

    @InjectMocks
    private StdSubtypeResolver resolver;
    private MapperConfig config;
    private AnnotationIntrospector introspector;
    private AnnotatedClass annotatedClass;
    private AnnotatedMember annotatedMember;
    private JavaType javaType;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        introspector = mock(AnnotationIntrospector.class);
        config = new Builder().build();
        config.setAnnotationIntrospector(introspector);
        javaType = mock(JavaType.class);
        annotatedClass = mock(AnnotatedClass.class);
        annotatedMember = mock(AnnotatedMember.class);
    }

    // Test for registerSubtypes(NamedType...) method
    @Test
    public void testRegisterSubtypes() {
        NamedType type1 = Mockito.mock(NamedType.class);
        NamedType type2 = Mockito.mock(NamedType.class);
        resolver.registerSubtypes(type1, type2);
        Assert.assertEquals(2, resolver._registeredSubtypes.size());
    }

    // Test for registerSubtypes(Class...) method
    @Test
    public void testRegisterSubtypesClassArray() {
        Class<?> clazz1 = Mockito.mock(Class.class);
        Class<?> clazz2 = Mockito.mock(Class.class);
        resolver.registerSubtypes(clazz1, clazz2);
        Assert.assertEquals(2, resolver._registeredSubtypes.size());
    }

    // Test for collectAndResolveSubtypesByClass(MapperConfig, AnnotatedMember, JavaType) method
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        when(introspector.findSubtypes(annotatedMember)).thenReturn(Collections.emptyList());

        when(javaType.getRawClass()).thenReturn(Mockito.mock(Class.class));
        when(annotatedClass.getRawType()).thenReturn(Mockito.mock(Class.class));

        when(annotatedClass.getName()).thenReturn("ClassName");

        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, annotatedMember, javaType);
        Assert.assertEquals(1, result.size());
    }

    // Test for collectAndResolveSubtypesByClass(MapperConfig, AnnotatedClass) method
    @Test
    public void testCollectAndResolveSubtypesByClass_AnnotatedClass() {
        when(introspector.findSubtypes(annotatedClass)).thenReturn(Collections.emptyList());
        when(annotatedClass.getName()).thenReturn("ClassName");

        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, annotatedClass);
        Assert.assertEquals(1, result.size());
    }

    // Test for collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedMember, JavaType) method with empty subtypes list
    @Test
    public void testCollectAndResolveSubtypesByTypeId_EmptySubtypesList() {
        when(introspector.findSubtypes(annotatedMember)).thenReturn(Collections.emptyList());

        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, annotatedMember, javaType);
        Assert.assertEquals(0, result.size());
    }

    // Test for collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass) method with empty subtypes list
    @Test
    public void testCollectAndResolveSubtypesByTypeId_EmptySubtypesList_AnnotatedClass() {
        when(introspector.findSubtypes(annotatedClass)).thenReturn(Collections.emptyList());

        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass);
        Assert.assertEquals(0, result.size());
    }

    // Test for collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedMember, JavaType) method with registered subtypes
    @Test
    public void testCollectAndResolveSubtypesByTypeId_RegisteredSubtypes() {
        when(introspector.findSubtypes(annotatedMember)).thenReturn(Collections.emptyList());
        when(javaType.getRawClass()).thenReturn(Mockito.mock(Class.class));

        NamedType registeredType1 = Mockito.mock(NamedType.class);
        NamedType registeredType2 = Mockito.mock(NamedType.class);

        when(introspector.findSubtypes(javaType.getRawClass())).thenReturn(Arrays.asList(registeredType1, registeredType2));

        when(registeredType1.getName()).thenReturn("RegisteredType1");
        when(registeredType2.getName()).thenReturn("RegisteredType2");

        resolver.registerSubtypes(registeredType1, registeredType2);

        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, annotatedMember, javaType);
        Assert.assertEquals(2, result.size());
    }

    // Test for collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass) method with registered subtypes
    @Test
    public void testCollectAndResolveSubtypesByTypeId_RegisteredSubtypes_AnnotatedClass() {
        when(introspector.findSubtypes(annotatedClass)).thenReturn(Collections.emptyList());
        when(annotatedClass.getName()).thenReturn("ClassName");
        when(annotatedClass.getRawType()).thenReturn(Mockito.mock(Class.class));

        NamedType registeredType1 = Mockito.mock(NamedType.class);
        NamedType registeredType2 = Mockito.mock(NamedType.class);

        when(introspector.findSubtypes(annotatedClass.getRawType())).thenReturn(Arrays.asList(registeredType1, registeredType2));

        when(registeredType1.getName()).thenReturn("RegisteredType1");
        when(registeredType2.getName()).thenReturn("RegisteredType2");

        resolver.registerSubtypes(registeredType1, registeredType2);

        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass);
        Assert.assertEquals(2, result.size());
    }
}