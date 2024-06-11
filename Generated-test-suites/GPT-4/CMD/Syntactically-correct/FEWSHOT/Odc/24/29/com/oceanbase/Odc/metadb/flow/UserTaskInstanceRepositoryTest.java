package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository repository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = repository.findByStatus(FlowNodeStatus.STARTED);
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("Test1", "Test2"));
        assertNotNull(result);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> result =  repository.findByResourceRoleIdentifierIn(Arrays.asList("Test1", "Test2"));
        assertNotNull(result);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> result = repository.findByRoleId(1L);
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserId(1L);
        assertNotNull(result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndActivityId(FlowNodeType.START, "TestActivity", 1L);
        assertNotNull(result);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndName(FlowNodeType.START, "Test", 1L);
        assertNotNull(result);
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = repository.findConfirmById(1L);
        assertNotNull(result);
    }
    
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> result = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.STARTED);
        assertNotNull(result);
    }
}
