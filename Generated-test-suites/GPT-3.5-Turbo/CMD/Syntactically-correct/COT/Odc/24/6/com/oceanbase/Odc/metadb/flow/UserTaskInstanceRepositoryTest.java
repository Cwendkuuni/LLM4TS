
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity userTaskInstance;

    @Before
    public void setUp() {
        userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setUserTaskId("123456");
        userTaskInstance.setStatus(FlowNodeStatus.PENDING);
        userTaskInstance.setApproved(false);
        userTaskInstance.setOperatorId(1L);
        userTaskInstance.setComment("Test comment");
        userTaskInstance.setExpireIntervalSeconds(60);
        userTaskInstance.setExternalFlowInstanceId("789");
        
        entityManager.persist(userTaskInstance);
        entityManager.flush();
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTasks = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);
        assertNotNull(userTasks);
        assertFalse(userTasks.isEmpty());
        assertEquals(1, userTasks.size());
        assertEquals(userTaskInstance.getStatus(), userTasks.get(0).getStatus());
    }

    // Add more test cases for other public methods...

    // Test for edge cases and exception handling scenarios...

}

