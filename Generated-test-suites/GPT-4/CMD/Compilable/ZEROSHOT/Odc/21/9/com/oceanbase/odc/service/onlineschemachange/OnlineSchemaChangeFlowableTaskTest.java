package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private DelegateExecution delegateExecution;
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
    }

    @Test
    public void startTest() throws Exception {
        onlineSchemaChangeFlowableTask.start(anyLong(), any(), delegateExecution);
        verify(onlineSchemaChangeFlowableTask, times(1)).start(anyLong(), any(), delegateExecution);
    }

    @Test
    public void isSuccessfulTest() {
        onlineSchemaChangeFlowableTask.isSuccessful();
        verify(onlineSchemaChangeFlowableTask, times(1)).isSuccessful();
    }

    @Test
    public void isFailureTest() {
        onlineSchemaChangeFlowableTask.isFailure();
        verify(onlineSchemaChangeFlowableTask, times(1)).isFailure();
    }

    @Test
    public void onFailureTest() throws Exception {
        onlineSchemaChangeFlowableTask.onFailure(anyLong(), any());
        verify(onlineSchemaChangeFlowableTask, times(1)).onFailure(anyLong(), any());
    }

    @Test
    public void onSuccessfulTest() throws Exception {
        onlineSchemaChangeFlowableTask.onSuccessful(anyLong(), any());
        verify(onlineSchemaChangeFlowableTask, times(1)).onSuccessful(anyLong(), any());
    }

    @Test
    public void onTimeoutTest() throws Exception {
        onlineSchemaChangeFlowableTask.onTimeout(anyLong(), any());
        verify(onlineSchemaChangeFlowableTask, times(1)).onTimeout(anyLong(), any());
    }

    @Test
    public void onProgressUpdateTest() throws Exception {
        onlineSchemaChangeFlowableTask.onProgressUpdate(anyLong(), any());
        verify(onlineSchemaChangeFlowableTask, times(1)).onProgressUpdate(anyLong(), any());
    }

    @Test
    public void cancelTest() throws Exception {
        onlineSchemaChangeFlowableTask.cancel(anyBoolean(), anyLong(), any());
        verify(onlineSchemaChangeFlowableTask, times(1)).cancel(anyBoolean(), anyLong(), any());
    }

    @Test
    public void isCancelledTest() {
        onlineSchemaChangeFlowableTask.isCancelled();
        verify(onlineSchemaChangeFlowableTask, times(1)).isCancelled();
    }
}
