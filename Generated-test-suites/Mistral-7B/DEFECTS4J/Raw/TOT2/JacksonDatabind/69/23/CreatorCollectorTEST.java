import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.MapperFactoryInstance;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() throws Exception {
        MapperFactoryInstance factory = new MapperFactoryInstance(new MapperConfig(ObjectMapper.DefaultTyping.JAVA_LANG_BEANS, new ObjectMapper.DefaultPropertyInclusion(JsonInclude.Include.NON_NULL)));
        CreatorCollector collector = new CreatorCollector(new BeanDescription(TestBean.class, factory), factory);

        Class<TestBean> testBeanClass = TestBean.class;
        Constructor<TestBean> noArgConstructor = testBeanClass.getDeclaredConstructor();
        noArgConstructor.setAccessible(true);
        AnnotatedWithParams noArgCreator = new AnnotatedWithParams(noArgConstructor, null, null, new Class[0], null, null, null);
        collector.setDefaultCreator(noArgCreator);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean obj = instantiator.createValueInstance(null);
        assertNotNull(obj);
    }

    @Test
    public void testStringCreator() throws Exception {
        MapperFactoryInstance factory = new MapperFactoryInstance(new MapperConfig(ObjectMapper.DefaultTyping.JAVA_LANG_BEANS, new ObjectMapper.DefaultPropertyInclusion(JsonInclude.Include.NON_NULL)));
        CreatorCollector collector = new CreatorCollector(new BeanDescription(TestBean.class, factory), factory);

        Class<TestBean> testBeanClass = TestBean.class;
        Constructor<TestBean> stringConstructor = testBeanClass.getDeclaredConstructor(String.class);
        stringConstructor.setAccessible(true);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringConstructor, null, null, new Class[]{String.class}, null, null, null);
        collector.addStringCreator(stringCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean obj = (TestBean) instantiator.createValueInstance("Test");
        assertNotNull(obj);
        assertEquals("Test", obj.getStr());
    }

    @Test
    public void testIntCreator() throws Exception {
        MapperFactoryInstance factory = new MapperFactoryInstance(new MapperConfig(ObjectMapper.DefaultTyping.JAVA_LANG_BEANS, new ObjectMapper.DefaultPropertyInclusion(JsonInclude.Include.NON_NULL)));
        CreatorCollector collector = new CreatorCollector(new BeanDescription(TestBean.class, factory), factory);

        Class<TestBean> testBeanClass = TestBean.class;
        Constructor<TestBean> intConstructor = testBeanClass.getDeclaredConstructor(int.class);
        intConstructor.setAccessible(true);
        AnnotatedWithParams intCreator = new AnnotatedWithParams(intConstructor, null, null, new Class[]{int.class}, null, null, null);
        collector.addIntCreator(intCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean obj = (TestBean) instantiator.createValueInstance(42);
        assertNotNull(obj);
        assertEquals(42, obj.getIntField());
    }

    @Test
    public void testLongCreator() throws Exception {
        MapperFactoryInstance factory = new MapperFactoryInstance(new MapperConfig(ObjectMapper.DefaultTyping.JAVA_LANG_BEANS, new ObjectMapper.DefaultPropertyInclusion(JsonInclude.Include.NON_NULL)));
        CreatorCollector collector = new CreatorCollector(new BeanDescription(TestBean.class, factory), factory);

        Class<TestBean> testBeanClass = TestBean.class;
        Constructor<TestBean> longConstructor = testBeanClass.getDeclaredConstructor(long.class);
        longConstructor.setAccessible(true);
        AnnotatedWithParams longCreator = new AnnotatedWithParams(longConstructor, null, null, new Class[]{long.class}, null, null, null);
        collector.addLongCreator(longCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean obj = (TestBean) instantiator.createValueInstance(42L);
        assertNotNull(obj);
        assertEquals(42L, obj.getLongField());
    }

    @Test
    public void testDoubleCreator() throws Exception {
        MapperFactoryInstance factory = new MapperFactoryInstance(new MapperConfig(ObjectMapper.DefaultTyping.JAVA_LANG_BEANS, new ObjectMapper.DefaultPropertyInclusion(JsonInclude.Include.NON_NULL)));
        CreatorCollector collector = new CreatorCollector(new BeanDescription(TestBean.class, factory), factory);

        Class<TestBean> testBeanClass = TestBean.class;
        Constructor<TestBean> doubleConstructor = testBeanClass.getDeclaredConstructor(double.class);
        doubleConstructor.setAccessible(true);
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(doubleConstructor, null, null, new Class[]{double.class}, null, null, null);
        collector.addDoubleCreator(doubleCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean obj = (TestBean) instantiator.createValueInstance(42.0);
        assertNotNull(obj);
        assertEquals(42.0, obj.getDoubleField(), 0);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        MapperFactoryInstance factory = new MapperFactoryInstance(new MapperConfig(ObjectMapper.DefaultTyping.JAVA_LANG_BEANS, new ObjectMapper.DefaultPropertyInclusion(JsonInclude.Include.NON_NULL)));
        CreatorCollector collector = new CreatorCollector(new BeanDescription(TestBean.class, factory), factory);

        Class<TestBean> testBeanClass = TestBean.class;
        Constructor<TestBean> booleanConstructor = testBeanClass.getDeclaredConstructor(boolean.class);
        booleanConstructor.setAccessible(true);
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(booleanConstructor, null, null, new Class[]{boolean.class}, null, null, null);
        collector.addBooleanCreator(booleanCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean obj = (TestBean) instantiator.createValueInstance(true);
        assertNotNull(obj);
        assertEquals(true, obj.getBoolField());
    }

    @Test
    public void testDelegateCreator() throws Exception {
        MapperFactoryInstance factory = new MapperFactoryInstance(new MapperConfig(ObjectMapper.DefaultTyping.JAVA_LANG_BEANS, new ObjectMapper.DefaultPropertyInclusion(JsonInclude.Include.NON_NULL)));
        CreatorCollector collector = new CreatorCollector(new BeanDescription(TestDelegateBean.class, factory), factory);

        Class<TestDelegateBean> testDelegateBeanClass = TestDelegateBean.class;
        Constructor<TestDelegateBean> delegateConstructor = testDelegateBeanClass.getDeclaredConstructor(TestBean.class);
        delegateConstructor.setAccessible(true);
        SettableBeanProperty delegateArg = new SettableBeanProperty("delegate", TestBean.class);
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(delegateConstructor, null, null, new Class[]{TestBean.class}, null, null, new SettableBeanProperty[]{delegateArg});
        collector.addDelegatingCreator(delegateCreator, true, new SettableBeanProperty[]{delegateArg});

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean testBean = new TestBean();
        TestDelegateBean obj = (TestDelegateBean) instantiator.createValueInstance(testBean);
        assertNotNull(obj);
        assertEquals(testBean, obj.getDelegate());
    }

    @Test
    public void testArrayDelegateCreator() throws Exception {
        MapperFactoryInstance factory = new MapperFactoryInstance(new MapperConfig(ObjectMapper.DefaultTyping.JAVA_LANG_BEANS, new ObjectMapper.DefaultPropertyInclusion(JsonInclude.Include.NON_NULL)));
        CreatorCollector collector = new CreatorCollector(new BeanDescription(TestArrayDelegateBean.class, factory), factory);

        Class<TestArrayDelegateBean> testArrayDelegateBeanClass = TestArrayDelegateBean.class;
        Constructor<TestArrayDelegateBean> arrayDelegateConstructor = testArrayDelegateBeanClass.getDeclaredConstructor(TestBean[].class);
        arrayDelegateConstructor.setAccessible(true);
        SettableBeanProperty[] arrayDelegateArgs = new SettableBeanProperty[1];
        arrayDelegateArgs[0] = new SettableBeanProperty("arrayDelegate", TestBean[].class);
        AnnotatedWithParams arrayDelegateCreator = new AnnotatedWithParams(arrayDelegateConstructor, null, null, new Class[]{TestBean[].class}, null, null, arrayDelegateArgs);
        collector.addDelegatingCreator(arrayDelegateCreator, true, arrayDelegateArgs);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean[] testBeans = new TestBean[1];
        testBeans[0] = new TestBean();
        TestArrayDelegateBean obj = (TestArrayDelegateBean) instantiator.createValueInstance(testBeans);
        assertNotNull(obj);
        assertArrayEquals(testBeans, obj.getArrayDelegate());
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        MapperFactoryInstance factory = new MapperFactoryInstance(new MapperConfig(ObjectMapper.DefaultTyping.JAVA_LANG_BEANS, new ObjectMapper.DefaultPropertyInclusion(JsonInclude.Include.NON_NULL)));
        CreatorCollector collector = new CreatorCollector(new BeanDescription(TestPropertyBasedBean.class, factory), factory);

        Class<TestPropertyBasedBean> testPropertyBasedBeanClass = TestPropertyBasedBean.class;
        Constructor<TestPropertyBasedBean> propertyBasedConstructor = testPropertyBasedBeanClass.getDeclaredConstructor();
        propertyBasedConstructor.setAccessible(true);
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[2];
        propertyBasedArgs[0] = new SettableBeanProperty("strProp", String.class);
        propertyBasedArgs[1] = new SettableBeanProperty("intProp", int.class);
        AnnotatedWithParams propertyBasedCreator = new AnnotatedWithParams(propertyBasedConstructor, null, null, new Class[0], null, null, propertyBasedArgs);
        collector.addPropertyCreator(propertyBasedCreator, true, propertyBasedArgs);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestPropertyBasedBean obj = (TestPropertyBasedBean) instantiator.createValueInstance();
        assertNotNull(obj);
        assertEquals("", obj.getStrProp());
        assertEquals(0, obj.getIntProp());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        MapperFactoryInstance factory = new MapperFactoryInstance(new MapperConfig(ObjectMapper.DefaultTyping.JAVA_LANG_BEANS, new ObjectMapper.DefaultPropertyInclusion(JsonInclude.Include.NON_NULL)));
        CreatorCollector collector = new CreatorCollector(new BeanDescription(TestBean.class, factory), factory);

        Class<TestBean> testBeanClass = TestBean.class;
        Constructor<TestBean> stringConstructor = testBeanClass.getDeclaredConstructor(String.class);
        stringConstructor.setAccessible(true);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringConstructor, null, null, new Class[]{String.class}, null, null, null);
        collector.addStringCreator(stringCreator, true);
        collector.addStringCreator(stringCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        // test should fail here
        instantiator.createValueInstance("Test");
    }

    @Test
    public void testNonDefaultCreator() throws Exception {
        MapperFactoryInstance factory = new MapperFactoryInstance(new MapperConfig(ObjectMapper.DefaultTyping.JAVA_LANG_BEANS, new ObjectMapper.DefaultPropertyInclusion(JsonInclude.Include.NON_NULL)));
        CreatorCollector collector = new CreatorCollector(new BeanDescription(TestNonDefaultBean.class, factory), factory);

        Class<TestNonDefaultBean> testNonDefaultBeanClass = TestNonDefaultBean.class;
        Constructor<TestNonDefaultBean> nonDefaultConstructor = testNonDefaultBeanClass.getDeclaredConstructor(int.class, String.class);
        nonDefaultConstructor.setAccessible(true);
        AnnotatedWithParams nonDefaultCreator = new AnnotatedWithParams(nonDefaultConstructor, null, null, new Class[]{int.class, String.class}, null, null, null);
        collector.addIntCreator(new AnnotatedWithParams(nonDefaultConstructor, null, null, new Class[]{int.class, String.class}, null, null, null), false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestNonDefaultBean obj = (TestNonDefaultBean) instantiator.createValueInstance(42, "Test");
        assertNotNull(obj);
        assertEquals(42, obj.getIntField());
        assertEquals("Test", obj.getStrField());
    }
}