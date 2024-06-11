import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {
    
    // initialize required objects here

    @Before
    public void setUp() {
        // add setup code here
    }

    @Test
    public void testIsContinueOnError() {
        // test in the case errorStrategy == CONTINUE
        // test in the case errorStrategy != CONTINUE
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // test when the sqlContent can be properly split into SQLs,
        // when sqlType == ALTER, 
        // when there's any exception during process, etc.
    }

    @After
    public void tearDown() {
        // add tear down code here
    }
}