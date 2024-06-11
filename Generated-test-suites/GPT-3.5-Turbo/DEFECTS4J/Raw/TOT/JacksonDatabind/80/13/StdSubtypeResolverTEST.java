import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;
    private MapperConfig<?> config;
    private AnnotatedMember property;
    private JavaType baseType;

    @Before
    public void setup() {
        subtypeResolver = new StdSubtypeResolver();
        config = new TestMapperConfig();
        property = new TestAnnotatedMember();
        baseType = null;
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType namedType1 = new NamedType(TestClass1.class);
        NamedType namedType2 = new NamedType(TestClass2.class);
        NamedType namedType3 = new NamedType(TestClass3.class);

        subtypeResolver.registerSubtypes(namedType1, namedType2, namedType3);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertEquals(3, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        namedType1 = new NamedType(TestClass1.class, "testClass1");
        namedType2 = new NamedType(TestClass2.class, "testClass2");

        subtypeResolver.registerSubtypes(namedType1, namedType2);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property.getRawType());
        assertEquals(2, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        TestAnnotatedClass type = new TestAnnotatedClass(TestClass1.class);

        subtypeResolver.registerSubtypes(TestClass1.class);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertEquals(1, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByIds() {
        namedType1 = new NamedType(TestClass1.class, "testClass1");
        namedType2 = new NamedType(TestClass2.class, "testClass2");

        subtypeResolver.registerSubtypes(namedType1, namedType2);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, new TestAnnotatedClass(TestBaseClass.class));
        assertEquals(2, subtypes.size());
    }

    private class TestMapperConfig implements MapperConfig<Object> {

        @Override
        public Class<?> getActiveView() {
            return null;
        }

        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return null;
        }

        @Override
        public MapperConfig<Object> withoutView() {
            return null;
        }

        @Override
        public <T> T getAttribute(Object key) {
            return null;
        }

        @Override
        public <T> T getAttribute(Object key, T defaultValue) {
            return null;
        }

        @Override
        public NullValueProvider getNullValueProvider() {
            return null;
        }

        @Override
        public Class<?> getSerializationView() {
            return null;
        }

        @Override
        public int getAnnotationIntrospectorFeatures() {
            return 0;
        }

        @Override
        public abstract boolean shouldSortPropertiesAlphabetically();

        @Override
        public abstract PropertyNamingStrategy getPropertyNamingStrategy();

        @Override
        public abstract MapperConfig<?> with(PropertyNamingStrategy strategy);

        @Override
        public abstract MapperConfig<?> with(PropertyNamingStrategy.PascalCaseStrategy strategy);

        @Override
        public abstract int getDefaultPropertyInclusion();

        @Override
        public abstract boolean isAnnotationProcessingEnabled();

        @Override
        public abstract boolean canOverrideAccessModifiers();

        @Override
        public abstract MapperConfig<?> with(MapperFeature... features);

        @Override
        public abstract MapperConfig<?> without(MapperFeature... features);

        @Override
        public abstract MapperConfig<?> with(SerializationFeature feature);

        @Override
        public abstract MapperConfig<?> without(SerializationFeature feature);

        @Override
        public abstract MapperConfig<?> with(DeserializationFeature feature);

        @Override
        public abstract MapperConfig<?> without(DeserializationFeature feature);

        @Override
        public abstract MapperConfig<?> with(StreamReadFeature feature);

        @Override
        public abstract MapperConfig<?> without(StreamReadFeature feature);

        @Override
        public abstract MapperConfig<?> with(StreamWriteFeature feature);

        @Override
        public abstract MapperConfig<?> without(StreamWriteFeature feature);

        @Override
        public abstract MapperConfig<?> with(Feature feature);

        @Override
        public abstract MapperConfig<?> without(Feature feature);

        @Override
        public abstract MapperConfig<?> with(ContextAttributes attrs);

        @Override
        public abstract MapperConfig<?> with(ContextAttributes.ProblemHandler h);

        @Override
        public abstract boolean hasMapperFeatures(int featureMask);

        @Override
        public abstract boolean hasSerializationFeatures(int featureMask);

        @Override
        public abstract boolean isEnabled(com.fasterxml.jackson.core.JsonFactory.Feature f);

        @Override
        public abstract boolean isEnabled(MapperFeature f);

        @Override
        public abstract boolean isEnabled(SerializationFeature f);

        @Override
        public abstract boolean isEnabled(SerializationFeature feature, Blocker... blockers);

        @Override
        public abstract boolean isEnabled(DeserializationFeature f);

        @Override
        public abstract boolean isEnabled(DeserializationFeature feature, Blocker... blockers);

        @Override
        public abstract boolean isEnabled(StreamReadFeature f);

        @Override
        public abstract boolean isEnabled(StreamWriteFeature f);

        @Override
        public abstract boolean isEnabled(JsonParser.Feature f);

        @Override
        public abstract boolean isEnabled(JsonGenerator.Feature f);

        @Override
        public abstract boolean hasDeserializationFeatures(int featureMask);

        @Override
        public abstract boolean hasSomeOfFeatures(int featureMask);

        @Override
        public abstract boolean canHandleFullRefinement();

        @Override
        public abstract boolean canSuppressByType(ObjectReader reader, AtomicReference<JavaType> rootType);
    }

    private class TestAnnotatedClass extends AnnotatedClass {

        public TestAnnotatedClass(Class<?> cls) {
            super(cls, null, null, null);
        }

        @Override
        public Class<?> getRawType() {
            return null;
        }
    }

    private class TestAnnotatedMember extends AnnotatedMember {

        @Override
        public Class<?> getRawType() {
            return null;
        }
    }

    private static class TestBaseClass {
    }

    private static class TestClass1 extends TestBaseClass {
    }

    private static class TestClass2 extends TestBaseClass {
    }

    private static class TestClass3 extends TestBaseClass {
    }

}