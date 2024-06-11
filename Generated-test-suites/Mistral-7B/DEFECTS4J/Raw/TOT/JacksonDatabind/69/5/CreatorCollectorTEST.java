package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import static org.testng.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() throws Exception {
        // Given
        ClassIdResolver idResolver = new ClassIdResolver() {
            @Override
            public String idFromClass(Class<?> c) {
                return c.getName();
            }
        };
        MapperConfig config = new MapperFactory(idResolver).configure(MapperFeature.AUTO_DETECT_CREATORS, false);
        BeanDescription beanDesc = Introspector.findBeanDescription(HashMap.class, config);

        // When
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Then
        Object result = instantiator.createValue(config, null);
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testStringCreator() throws Exception {
        // Given
        ClassIdResolver idResolver = new ClassIdResolver() {
            @Override
            public String idFromClass(Class<?> c) {
                return c.getName();
            }
        };
        MapperConfig config = new MapperFactory(idResolver).configure(MapperFeature.AUTO_DETECT_CREATORS, false);
        BeanDescription beanDesc = Introspector.findBeanDescription(LinkedHashMap.class, config);
        Method stringCreator = LinkedHashMap.class.getDeclaredMethod("newInstance", String.class);

        // When
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        collector.addStringCreator(Utils.methodToAnnotatedWithParams(stringCreator), true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Then
        Object result = instantiator.createValue(config, "test");
        assertTrue(result instanceof LinkedHashMap);
        assertEquals(result, new LinkedHashMap<>());
    }

    @Test
    public void testIntCreator() throws Exception {
        // Given
        ClassIdResolver idResolver = new ClassIdResolver() {
            @Override
            public String idFromClass(Class<?> c) {
                return c.getName();
            }
        };
        MapperConfig config = new MapperFactory(idResolver).configure(MapperFeature.AUTO_DETECT_CREATORS, false);
        BeanDescription beanDesc = Introspector.findBeanDescription(ArrayList.class, config);
        Method intCreator = ArrayList.class.getDeclaredMethod("newInstance", int.class);

        // When
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        collector.addIntCreator(Utils.methodToAnnotatedWithParams(intCreator), true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Then
        Object result = instantiator.createValue(config, 5);
        assertTrue(result instanceof ArrayList);
        assertEquals(result, new ArrayList<>(5));
    }

    @Test
    public void testBooleanCreator() throws Exception {
        // Given
        ClassIdResolver idResolver = new ClassIdResolver() {
            @Override
            public String idFromClass(Class<?> c) {
                return c.getName();
            }
        };
        MapperConfig config = new MapperFactory(idResolver).configure(MapperFeature.AUTO_DETECT_CREATORS, false);
        BeanDescription beanDesc = Introspector.findBeanDescription(Boolean.class, config);
        Method booleanCreator = Boolean.class.getDeclaredMethod("valueOf", String.class);

        // When
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        collector.addBooleanCreator(Utils.methodToAnnotatedWithParams(booleanCreator), true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Then
        Object result = instantiator.createValue(config, "true");
        assertTrue(result instanceof Boolean);
        assertEquals(result, Boolean.TRUE);
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        // Given
        ClassIdResolver idResolver = new ClassIdResolver() {
            @Override
            public String idFromClass(Class<?> c) {
                return c.getName();
            }
        };
        MapperConfig config = new MapperFactory(idResolver).configure(MapperFeature.AUTO_DETECT_CREATORS, false);

        Class<User> userType = User.class;
        AnnotatedWithParams userCreator = Utils.methodToAnnotatedWithParams(User.class.getDeclaredConstructor());

        BeanDescription beanDesc = Introspector.findBeanDescription(User.class, config);
        SettableBeanProperty userNameProp = new AnnotatedStandardBeanProperty(beanDesc, "name");

        // When
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        collector.addDelegatingCreator(userCreator, true, new SettableBeanProperty[]{userNameProp});
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Then
        Object[] args = new Object[]{new String("Alice")};
        Object result = instantiator.createValue(config, args);
        assertTrue(result instanceof User);
        assertEquals(result, new User("Alice"));
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        // Given
        ClassIdResolver idResolver = new ClassIdResolver() {
            @Override
            public String idFromClass(Class<?> c) {
                return c.getName();
            }
        };
        MapperConfig config = new MapperFactory(idResolver).configure(MapperFeature.AUTO_DETECT_CREATORS, false);

        Class<User> userType = User.class;
        AnnotatedWithParams userCreator = Utils.methodToAnnotatedWithParams(User.class.getDeclaredConstructor());

        BeanDescription beanDesc = Introspector.findBeanDescription(User.class, config);
        SettableBeanProperty userNameProp = new AnnotatedStandardBeanProperty(beanDesc, "name");
        SettableBeanProperty userAgeProp = new AnnotatedStandardBeanProperty(beanDesc, "age");

        // When
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        collector.addPropertyCreator(userCreator, true, new SettableBeanProperty[]{userNameProp, userAgeProp});
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Then
        Object[] args = new Object[]{"Alice", 30};
        Object result = instantiator.createValue(config, args);
        assertTrue(result instanceof User);
        assertEquals(result, new User("Alice", 30));
    }

    @Test
    public void testArrayDelegateCreator() throws Exception {
        // Given
        ClassIdResolver idResolver = new ClassIdResolver() {
            @Override
            public String idFromClass(Class<?> c) {
                return c.getName();
            }
        };
        MapperConfig config = new MapperFactory(idResolver).configure(MapperFeature.AUTO_DETECT_CREATORS, false);

        Class<ArrayUser> arrayUserType = ArrayUser[].class;
        AnnotatedWithParams arrayUserCreator = Utils.methodToAnnotatedWithParams(ArrayUser[].class.getDeclaredConstructor());

        BeanDescription beanDesc = Introspector.findBeanDescription(ArrayUser[].class, config);
        SettableBeanProperty[] arrayUserProps = new SettableBeanProperty[]{
            new AnnotatedStandardBeanProperty(beanDesc, "users")
        };

        // When
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        collector.addArrayDelegateCreator(arrayUserCreator, true, arrayUserProps);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Then
        Object[] args = new Object[]{new User[] {new User("Alice"), new User("Bob")}};
        Object result = instantiator.createValue(config, args);
        assertTrue(result instanceof ArrayUser);
        assertEquals(result, new ArrayUser[] {new User("Alice"), new User("Bob")});
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testDuplicateCreator() throws Exception {
        // Given
        ClassIdResolver idResolver = new ClassIdResolver() {
            @Override
            public String idFromClass(Class<?> c) {
                return c.getName();
            }
        };
        MapperConfig config = new MapperFactory(idResolver).configure(MapperFeature.AUTO_DETECT_CREATORS, false);

        Class<User> userType = User.class;
        AnnotatedWithParams userCreator = Utils.methodToAnnotatedWithParams(User.class.getDeclaredConstructor());

        BeanDescription beanDesc = Introspector.findBeanDescription(User.class, config);
        SettableBeanProperty userNameProp = new AnnotatedStandardBeanProperty(beanDesc, "name");

        // When
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        collector.addPropertyCreator(userCreator, true, new SettableBeanProperty[]{userNameProp});
        collector.addPropertyCreator(userCreator, true, new SettableBeanProperty[]{userNameProp});
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Then: should throw an IllegalArgumentException
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testNonAnnotatedCreator() throws Exception {
        // Given
        ClassIdResolver idResolver = new ClassIdResolver() {
            @Override
            public String idFromClass(Class<?> c) {
                return c.getName();
            }
        };
        MapperConfig config = new MapperFactory(idResolver).configure(MapperFeature.AUTO_DETECT_CREATORS, false);

        Class<User> userType = User.class;
        Method nonAnnotatedCreator = User.class.getDeclaredMethod("create", String.class);

        BeanDescription beanDesc = Introspector.findBeanDescription(User.class, config);

        // When
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        collector.addStringCreator(Utils.methodToAnnotatedWithParams(nonAnnotatedCreator), false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Then: should throw an IllegalArgumentException
    }

    @Test
    public void testCustomCreatorOrder() throws Exception {
        // Given
        ClassIdResolver idResolver = new ClassIdResolver() {
            @Override
            public String idFromClass(Class<?> c) {
                return c.getName();
            }
        };
        MapperConfig config = new MapperFactory(idResolver).configure(MapperFeature.AUTO_DETECT_CREATORS, false);

        Class<User> userType = User.class;
        AnnotatedWithParams userCreator1 = Utils.methodToAnnotatedWithParams(User.class.getDeclaredConstructor());
        AnnotatedWithParams userCreator2 = Utils.methodToAnnotatedWithParams(User.class.getDeclaredConstructor(String.class));

        BeanDescription beanDesc = Introspector.findBeanDescription(User.class, config);

        // When
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        collector.addStringCreator(userCreator2, false);
        collector.addStringCreator(userCreator1, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Then
        Object result1 = instantiator.createValue(config, null);
        assertEquals(result1, new User());
        Object result2 = instantiator.createValue(config, "Alice");
        assertEquals(result2, new User("Alice"));
    }

    // Helper methods

    private AnnotatedWithParams Utils_methodToAnnotatedWithParams(Method method) {
        return new AnnotatedMethod(method, new AnnotatedParameterList(Arrays.asList(method.getParameters())));
    }

    private static final class AnnotatedMethod extends AnnotatedExecutable {
        protected final Method _method;

        public AnnotatedMethod(Method method, AnnotatedParameterList p) {
            super(method, null, p);
            _method = method;
        }

        @Override
        public Object invoke(Object c, Object[] args) throws Throwable {
            return _method.invoke(c, args);
        }
    }

    private static final class AnnotatedParameter extends AnnotatedElementBase {
        protected final Object _value;

        public AnnotatedParameter(Class<?> type, String name, int index) {
            super(type);
            _value = name != null ? new AnnotatedField(getClass(), name, null, null) : null;
        }

        @Override
        public String getName() {
            return _value != null ? _value.getName() : Integer.toString(getIndex());
        }

        @Override
        public int getIndex() {
            return _index;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            return _index == ((AnnotatedParameter) o)._index;
        }

        @Override
        public int hashCode() {
            return _index;
        }

        private int _index;
    }

    private static class AnnotatedWithParams extends AnnotatedMethod {
        private final AnnotatedParameterList _params;

        public AnnotatedWithParams(Method method, AnnotatedParameterList params) {
            super(method, params);
            _params = params;
        }

        @Override
        public AnnotatedParameterList getParameters() {
            return _params;
        }
    }

}