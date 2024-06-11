import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository instanceRepository;

    @Before
    public void setUp() {
        instanceRepository = new UserTaskInstanceRepository();
    }

    @Test
    public void testFindByStatus() {
        // typical use case
        assertNotNull(instanceRepository.findByStatus(FlowNodeStatus.APPROVED));
        
        // edge case
        assertNotNull(instanceRepository.findByStatus(FlowNodeStatus.PENDING));
        
        // error scenario
        assertNull(instanceRepository.findByStatus(null));
    }

    // continue with similar tests for other methods...

}