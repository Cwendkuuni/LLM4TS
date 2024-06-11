import static org.junit.Assert.*;
import org.junit.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder pb = new PatternBuilder();
        assertNotNull(pb.create());
    }
    
    @Test
    public void testReset() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("!@:<#>/*");
        pb.reset();
        assertEquals(0, pb.options.size());
    }
    
    @Test
    public void testWithPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("!@:<#>/*");
        assertFalse(pb.options.isEmpty());
    }
    
    @Test
    public void testCreateOption() {
        PatternBuilder pb = new PatternBuilder();
        pb.createOption('!', true, 'o');
        assertFalse(pb.options.isEmpty());
    }
    
    @Test
    public void testValidator() {
        PatternBuilder pb = new PatternBuilder();
        assertNotNull(pb.validator('@'));
        assertNotNull(pb.validator('+'));
        assertNull(pb.validator(':'));
    }
    
    @Test
    public void testWithPatternTypicalUseCases() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("!@:<#>/*");
        assertNotNull(pb.create());
    }
    
    @Test
    public void testWithPatternEdgeCases() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("");
        assertNotNull(pb.create());
        
        PatternBuilder pb2 = new PatternBuilder();
        pb2.withPattern("a:b");
        assertNotNull(pb2.create());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testWithPatternErrorScenarios() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("()");
    }

}