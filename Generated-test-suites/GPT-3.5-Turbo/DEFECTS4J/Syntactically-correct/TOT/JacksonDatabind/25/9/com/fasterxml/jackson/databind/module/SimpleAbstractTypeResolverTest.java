import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;

public class SimpleAbstractTypeResolverTest {
    
    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        Class<ClassA> superType = ClassA.class;
        Class<ClassB> subType = ClassB.class;
        resolver.addMapping(superType, subType);
        
        Assert.assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSameClass() {
        Class<ClassA> superType = ClassA.class;
        Class<ClassA> subType = ClassA.class;
        resolver.addMapping(superType, subType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotSubType() {
        Class<ClassA> superType = ClassA.class;
        Class<ClassC> subType = ClassC.class;
        resolver.addMapping(superType, subType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingConcreteSuperType() {
        Class<ClassD> superType = ClassD.class;
        Class<ClassE> subType = ClassE.class;
        resolver.addMapping(superType, subType);
    }

    @Test
    public void testFindTypeMapping() {
        Class<ClassA> superType = ClassA.class;
        Class<ClassB> subType = ClassB.class;
        resolver.addMapping(superType, subType);
        JavaType javaType = new JavaType(ClassA.class);

        JavaType result = resolver.findTypeMapping(config, javaType);

        Assert.assertEquals(new JavaType(subType), result);
    }

    @Test
    public void testFindTypeMappingNotFound() {
        Class<ClassA> superType = ClassA.class;
        Class<ClassB> subType = ClassB.class;
        resolver.addMapping(superType, subType);
        JavaType javaType = new JavaType(ClassC.class);

        JavaType result = resolver.findTypeMapping(config, javaType);

        Assert.assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        JavaType javaType = new JavaType(ClassA.class);

        JavaType result = resolver.resolveAbstractType(config, javaType);

        Assert.assertNull(result);
    }
    
    private static class ClassA {}
    
    private static class ClassB extends ClassA {}

    private static class ClassC {}

    private static class ClassD {}

    private static abstract class ClassE extends ClassD {}
}