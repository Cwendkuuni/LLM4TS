
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository repository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> tasks =  repository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(tasks);
        //add more assertions based on the expectation
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> tasks = repository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L,2L));
        assertNotNull(tasks);
        //add more assertions based on the expectation
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> tasks = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));
        assertNotNull(tasks);
        //add more assertions based on the expectation
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> tasks = repository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
        assertNotNull(tasks);
        //add more assertions based on the expectation
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> tasks = repository.findByRoleId(1L);
        assertNotNull(tasks);
        //add more assertions based on the expectation
    }
    
    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> tasks = repository.findByCandidateUserId(1L);
        assertNotNull(tasks);
        //add more assertions based on the expectation
    }

    //Continue with the rest of the methods in the same fashion...
}

