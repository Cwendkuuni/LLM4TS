/***  **/
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OscServiceTest {

    OscService oscService; // assuming it is instantiated elsewhere

    @Test
    void getOscDatabaseInfoTest() {
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L); 
        assertNotNull(result);

        // There should be more assertions here based on expected outcomes which depends
        // on the setup of OscService and the data in the mocked database
    }

    @Test
    void swapTableTest() {
        OscSwapTableVO result = oscService.swapTable(5L);
        assertNotNull(result);

        // More assertions based on expected outcomes and how possible exceptions are
        // handled, which depends on the setup of OscService and the data in the mocked database
    }

    /*** Implementing tests for edge cases, exceptions, 
         and the private getLockUserIsRequired method 
         would require additional context and setup ***/
}
/***  **/