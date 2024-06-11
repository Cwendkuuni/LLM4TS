package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.assertNotNull;
    import static org.mockito.Mockito.*;

    @RunWith(MockitoJUnitRunner.class)
    public class OscServiceTest {

        @InjectMocks 
        private OscService oscService;

        @Mock
        private DatabaseRepository databaseRepository;

        @Mock
        private ScheduleTaskRepository scheduleTaskRepository;

        @Mock
        private ScheduleRepository scheduleRepository;


        @Test
        public void getOscDatabaseInfoTest(){
            Long dbId = 123L;
            when(databaseRepository.findById(any(Long.class))).thenReturn(Optional.of(new DatabaseEntity()));

            OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(dbId);

            assertNotNull(result);
        }

        @Test
        public void swapTableTest(){
            Long taskId = 123L;
            ScheduleTaskEntity entity = new ScheduleTaskEntity();
            entity.setJobName("345");

            when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.of(entity));
            when(scheduleRepository.findById(any(Long.class))).thenReturn(Optional.of(new ScheduleEntity()));
    
            OscSwapTableVO result = oscService.swapTable(taskId);

            assertNotNull(result);
        }

    }