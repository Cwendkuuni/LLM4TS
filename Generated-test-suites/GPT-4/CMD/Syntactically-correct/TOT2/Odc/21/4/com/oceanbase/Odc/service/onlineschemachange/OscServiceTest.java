package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private OscService oscService;

    @Test
    public void getOscDatabaseInfo_DatabaseExists_ReturnsDataFromDatabaseEntity() {
        Long givenId = 1L;
        Optional<DatabaseEntity> expectedResult = Optional.of(new DatabaseEntity());

        when(databaseRepository.findById(givenId)).thenReturn(expectedResult);
        // When lock user is required, test another handled edge case
        when(oscService.getLockUserIsRequired(givenId)).thenReturn(false);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(givenId);

        assertThat(result.getLockDatabaseUserRequired()).isEqualTo(expectedResult.getBody());
        verify(databaseRepository).findById(givenId);
    }

    @Test
    public void getOscDatabaseInfo_DatabaseDoesNotExist_ReturnsEmptyDatabaseUserInfo() {
        Long givenId = 1L;
        Optional<DatabaseEntity> expectedResult = Optional.empty();

        when(databaseRepository.findById(givenId)).thenReturn(expectedResult);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(givenId);

        assertThat(result.getDatabaseId()).isNull();
        verify(databaseRepository).findById(givenId);
    }

    // Additional test case scenarios can also include:
    // Handling the edge case where manual swap table is disabled and should throw an appropriate error
    // Check whether the swap table function is correct under normal conditions
    // Check whether the swap table function is correct when a non-existent schedule task ID is given
    // Verify that the proper values are being modified/ set in the result object 
    // The exceptions that can be handled while getting whether lock user is needed or not.
}