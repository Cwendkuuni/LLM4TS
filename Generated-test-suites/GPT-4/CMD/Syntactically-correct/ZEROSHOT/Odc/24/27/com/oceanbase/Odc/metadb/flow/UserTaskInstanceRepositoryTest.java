
package com.oceanbase.odc.metadb.flow;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {
    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @MockBean
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;
    
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long dummyUserId = 1L;
        Collection<Long> dummyRoleIds = Collections.singleton(2L);
        userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(dummyUserId, dummyRoleIds);
        Mockito.verify(userTaskInstanceRepositoryMock).findByCandidateUserIdOrRoleIds(dummyUserId, dummyRoleIds);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long dummyUserId = 1L;
        Collection<String> dummyResourceRoleIdentifiers = Collections.singleton("ROLE_1");
        userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(dummyUserId, dummyResourceRoleIdentifiers);
        Mockito.verify(userTaskInstanceRepositoryMock).findByCandidateUserIdOrResourceRoleIdentifier(dummyUserId, dummyResourceRoleIdentifiers);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> dummyResourceRoleIdentifiers = Collections.singleton("ROLE_1");
        userTaskInstanceRepository.findByResourceRoleIdentifierIn(dummyResourceRoleIdentifiers);
        Mockito.verify(userTaskInstanceRepositoryMock).findByResourceRoleIdentifierIn(dummyResourceRoleIdentifiers);
    }

    @Test
    public void testFindByRoleId() {
        Long dummyRoleId = 1L;
        userTaskInstanceRepository.findByRoleId(dummyRoleId);
        Mockito.verify(userTaskInstanceRepositoryMock).findByRoleId(dummyRoleId);
    } 

    // Similar tests can be written for other methods.
}
