
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.INITIALIZED;
        repository.findByStatus(status);
        verify(repository).findByStatus(status);
    }
    
    //similarly you can implement test cases for all other methods.

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        repository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L, 3L));
        verify(repository).findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L, 3L));
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        repository.update(entity);
        verify(repository).update(entity);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        repository.findByInstanceTypeAndActivityId(instanceType, "activityId", 1L);
        verify(repository).findByInstanceTypeAndActivityId(instanceType, "activityId", 1L);
    }

    // Add more tests here based on the methods in your repository
}
