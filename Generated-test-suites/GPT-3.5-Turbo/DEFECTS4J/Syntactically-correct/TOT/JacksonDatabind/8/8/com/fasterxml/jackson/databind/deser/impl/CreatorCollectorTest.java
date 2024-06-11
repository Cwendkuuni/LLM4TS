import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector cc = new CreatorCollector();
        cc.setDefaultCreator(AnnotatedWithParams.creator1);
        assertSame(AnnotatedWithParams.creator1, cc.getDefaultCreator());
    }
    
    @Test
    public void testAddStringCreator() {
        CreatorCollector cc = new CreatorCollector();
        cc.addStringCreator(AnnotatedWithParams.creator2, false);
        assertSame(AnnotatedWithParams.creator2, cc.getStringCreator());
    }
    
    @Test
    public void testAddIntCreator() {
        CreatorCollector cc = new CreatorCollector();
        cc.addIntCreator(AnnotatedWithParams.creator3, false);
        assertSame(AnnotatedWithParams.creator3, cc.getIntCreator());
    }
    
    @Test
    public void testAddLongCreator() {
        CreatorCollector cc = new CreatorCollector();
        cc.addLongCreator(AnnotatedWithParams.creator4, false);
        assertSame(AnnotatedWithParams.creator4, cc.getLongCreator());
    }
    
    @Test
    public void testAddDoubleCreator() {
        CreatorCollector cc = new CreatorCollector();
        cc.addDoubleCreator(AnnotatedWithParams.creator5, false);
        assertSame(AnnotatedWithParams.creator5, cc.getDoubleCreator());
    }
    
    @Test
    public void testAddBooleanCreator() {
        CreatorCollector cc = new CreatorCollector();
        cc.addBooleanCreator(AnnotatedWithParams.creator6, false);
        assertSame(AnnotatedWithParams.creator6, cc.getBooleanCreator());
    }
    
    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector cc = new CreatorCollector();
        cc.addDelegatingCreator(AnnotatedWithParams.creator7, false, CreatorProperty.injectables);
        assertSame(AnnotatedWithParams.creator7, cc.getDelegatingCreator());
        assertArrayEquals(CreatorProperty.injectables, cc.getDelegateArgs());
    }
    
    @Test
    public void testAddPropertyCreator() {
        CreatorCollector cc = new CreatorCollector();
        cc.addPropertyCreator(AnnotatedWithParams.creator8, false, CreatorProperty.properties);
        assertSame(AnnotatedWithParams.creator8, cc.getPropertyCreator());
        assertArrayEquals(CreatorProperty.properties, cc.getPropertyBasedArgs());
    }
    
    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector cc = new CreatorCollector();
        cc.addIncompeteParameter(AnnotatedParameter.parameter);
        assertSame(AnnotatedParameter.parameter, cc.getIncompleteParameter());
    }
}