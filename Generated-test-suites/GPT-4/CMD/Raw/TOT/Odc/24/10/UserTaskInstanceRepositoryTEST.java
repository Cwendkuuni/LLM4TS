
package com.oceanbase.odc.metadb.flow;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.Mockito.when;

public class UserTaskInstanceRepositoryTest extends TestCase {

    @Mock
    UserTaskInstanceEntity userTaskInstanceEntityMock;

    @InjectMocks
    UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        when(userTaskInstanceRepositoryMock.findByStatus(FlowNodeStatus.ACTIVE))
                          .thenReturn(Arrays.asList(userTaskInstanceEntityMock));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        when(userTaskInstanceRepositoryMock.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L)))
                          .thenReturn(Arrays.asList(userTaskInstanceEntityMock));
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepositoryMock.update(userTaskInstanceEntity)).thenReturn(1);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(userTaskInstanceRepositoryMock.deleteByFlowInstanceId(1L)).thenReturn(1);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
       when(userTaskInstanceRepositoryMock.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L), FlowNodeStatus.ACTIVE))
                         .thenReturn(Arrays.asList(userTaskInstanceEntityMock));
    }

    // Continue with the rest of the method tests...
}
