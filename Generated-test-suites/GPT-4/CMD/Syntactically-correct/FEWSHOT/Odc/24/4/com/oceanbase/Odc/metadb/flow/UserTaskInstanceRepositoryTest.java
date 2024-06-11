
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PROCESSING);
        assertNotNull(entities);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // create test data
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        assertNotNull(entities);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        // create test data
        List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2", "role3");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository
                .findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
        assertNotNull(entities);
        assertFalse(entities.isEmpty());
    }

    // Similarly, you should create test cases for all other methods in UserTaskInstanceRepository.
}
