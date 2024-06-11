import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams defaultCreator = new AnnotatedWithParams();
        
        creatorCollector.setDefaultCreator(defaultCreator);
        
        assertEquals(defaultCreator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams();
        
        creatorCollector.addStringCreator(stringCreator, true);
        
        assertEquals(stringCreator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams intCreator = new AnnotatedWithParams();
        
        creatorCollector.addIntCreator(intCreator, true);
        
        assertEquals(intCreator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams longCreator = new AnnotatedWithParams();
        
        creatorCollector.addLongCreator(longCreator, true);
        
        assertEquals(longCreator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams();
        
        creatorCollector.addDoubleCreator(doubleCreator, true);
        
        assertEquals(doubleCreator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams();
        
        creatorCollector.addBooleanCreator(booleanCreator, true);
        
        assertEquals(booleanCreator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams delegatingCreator = new AnnotatedWithParams();
        CreatorCollector.CreatorProperty[] injectables = new CreatorCollector.CreatorProperty[1];
        
        creatorCollector.addDelegatingCreator(delegatingCreator, true, injectables);
        
        assertEquals(delegatingCreator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams propertyCreator = new AnnotatedWithParams();
        CreatorCollector.CreatorProperty[] properties = new CreatorCollector.CreatorProperty[1];
        
        creatorCollector.addPropertyCreator(propertyCreator, true, properties);
        
        assertEquals(propertyCreator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedParameter incompleteParameter = new AnnotatedParameter();
        
        creatorCollector.addIncompeteParameter(incompleteParameter);
        
        assertEquals(incompleteParameter, creatorCollector._incompleteParameter);
    }
}