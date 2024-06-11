package com.oceanbase.odc.metadb.flow;
import com.oceanbase.odc.metadb.flow.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.*;

public class UserTaskInstanceRepositoryTest {

    @MockBean
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.UNSTARTED;
        Mockito.when(userTaskInstanceRepository.findByStatus(status)).thenReturn(new ArrayList<>());
        assertEquals(userTaskInstanceRepository.findByStatus(status).size(), 0);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds))
          .thenReturn(new ArrayList<>());
        assertEquals(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds).size(), 0);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("ROLE_1", "ROLE_2");
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers))
          .thenReturn(new ArrayList<>());
        assertEquals(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers).size(), 0);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifiers = Arrays.asList("ROLE_1", "ROLE_2");
        Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers))
          .thenReturn(new ArrayList<>());
        assertEquals(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers).size(), 0);
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        Mockito.when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(new ArrayList<>());
        assertEquals(userTaskInstanceRepository.findByRoleId(roleId).size(), 0);
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(new ArrayList<>());
        assertEquals(userTaskInstanceRepository.findByCandidateUserId(userId).size(), 0);
    }

}