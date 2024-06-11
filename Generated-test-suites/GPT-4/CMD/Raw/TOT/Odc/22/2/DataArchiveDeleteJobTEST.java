/*
 * Copyright (c) 2023 OceanBase.
 * This software is licensed under the terms of the Apache License 2.0.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleTaskEntity taskEntity;

    //... More Mocking and Initialization Code Here ...

    @Test
    public void testDataArchiveDeleteJobExecute() { 
        
        when(context.getResult()).thenReturn(taskEntity); 

        DataArchiveDeleteJob job = new DataArchiveDeleteJob();

        job.execute(context);

        verify(taskEntity).getParametersJson();
        
        // you can put more verifications here depends on your implementation 
        // but this is a good start to see if the getParametersJson method gets called.
        
    } 
}