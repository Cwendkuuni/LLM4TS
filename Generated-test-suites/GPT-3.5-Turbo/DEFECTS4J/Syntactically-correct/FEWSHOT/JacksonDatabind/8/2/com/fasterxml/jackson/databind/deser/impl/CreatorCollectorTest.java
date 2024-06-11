import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testConstructor() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);
        assertEquals(beanDesc, creatorCollector._beanDesc);
        assertEquals(canFixAccess, creatorCollector._canFixAccess);
    }
    
    @Test
    public void testConstructValueInstantiator() {
        // TODO: Implement test case
    }
    
    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }
    
    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addStringCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }
    
    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addIntCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }
    
    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addLongCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }
    
    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addDoubleCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }
    
    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addBooleanCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }
    
    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector();
        CreatorProperty[] injectables = new CreatorProperty[1];
        creatorCollector.addDelegatingCreator(creator, true, injectables);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables, creatorCollector._delegateArgs);
    }
    
    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector();
        CreatorProperty[] properties = new CreatorProperty[1];
        creatorCollector.addPropertyCreator(creator, true, properties);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertEquals(properties, creatorCollector._propertyBasedArgs);
    }
    
    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector._creators[CreatorCollector.C_DEFAULT] = new AnnotatedWithParams();
        assertEquals(true, creatorCollector.hasDefaultCreator());
    }
}