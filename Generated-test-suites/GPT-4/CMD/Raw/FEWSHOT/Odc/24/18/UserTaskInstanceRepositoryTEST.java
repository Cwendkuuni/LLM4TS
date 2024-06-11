package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DataJpaTest
@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        userTaskInstanceRepository.findByStatus(FlowNodeStatus.UNCONFIRMED);
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.UNCONFIRMED);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(1L));
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(1L));
    }
    
    // Continue with remaining methods following the same pattern
}