package com.oceanbase.odc.metadb.flow;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock 
    UserTaskInstanceEntity userTaskInstanceEntity;

    @Test
    public void updateTest() {
        int result = userTaskInstanceRepository.update(userTaskInstanceEntity);
        assertTrue("Expected non-negative entity id after insert operation", result >= 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_Null_Test() {
        userTaskInstanceRepository.update(null);
    }
}