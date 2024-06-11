
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExecutionFilterTest {

	@Test
	public void testExecutionFilter() {
		ExecutionFilter filter = new ExecutionFilter();
		assertEquals(0, filter.m_clientId);
	}
	
	@Test
	public void testExecutionFilterWithParameters() {
		int clientId = 123;
		String acctCode = "ACCT123";
		String time = "2021-01-01";
		String symbol = "AAPL";
		String secType = "STK";
		String exchange = "NASDAQ";
		String side = "BUY";
		
		ExecutionFilter filter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
		
		assertEquals(clientId, filter.m_clientId);
		assertEquals(acctCode, filter.m_acctCode);
		assertEquals(time, filter.m_time);
		assertEquals(symbol, filter.m_symbol);
		assertEquals(secType, filter.m_secType);
		assertEquals(exchange, filter.m_exchange);
		assertEquals(side, filter.m_side);
	}
	
	@Test
	public void testEqualsNull() {
		ExecutionFilter filter = new ExecutionFilter();
		boolean result = filter.equals(null);
		assertEquals(false, result);
	}
	
	@Test
	public void testEqualsSameObject() {
		ExecutionFilter filter = new ExecutionFilter();
		boolean result = filter.equals(filter);
		assertEquals(true, result);
	}
	
	@Test
	public void testEqualsDifferentClientId() {
		ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STK", "NASDAQ", "BUY");
		ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT123", "2021-01-01", "AAPL", "STK", "NASDAQ", "BUY");
		boolean result = filter1.equals(filter2);
		assertEquals(false, result);
	}
	
	@Test
	public void testEqualsDifferentAcctCode() {
		ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STK", "NASDAQ", "BUY");
		ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT456", "2021-01-01", "AAPL", "STK", "NASDAQ", "BUY");
		boolean result = filter1.equals(filter2);
		assertEquals(false, result);
	}
	
	@Test
	public void testEqualsDifferentTime() {
		ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STK", "NASDAQ", "BUY");
		ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
		boolean result = filter1.equals(filter2);
		assertEquals(false, result);
	}
	
	@Test
	public void testEqualsDifferentSymbol() {
		ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STK", "NASDAQ", "BUY");
		ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "IBM", "STK", "NASDAQ", "BUY");
		boolean result = filter1.equals(filter2);
		assertEquals(false, result);
	}
	
	@Test
	public void testEqualsDifferentSecType() {
		ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STK", "NASDAQ", "BUY");
		ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "OPT", "NASDAQ", "BUY");
		boolean result = filter1.equals(filter2);
		assertEquals(false, result);
	}
	
	@Test
	public void testEqualsDifferentExchange() {
		ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STK", "NASDAQ", "BUY");
		ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STK", "NYSE", "BUY");
		boolean result = filter1.equals(filter2);
		assertEquals(false, result);
	}
	
	@Test
	public void testEqualsDifferentSide() {
		ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STK", "NASDAQ", "BUY");
		ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STK", "NASDAQ", "SELL");
		boolean result = filter1.equals(filter2);
		assertEquals(false, result);
	}
}

