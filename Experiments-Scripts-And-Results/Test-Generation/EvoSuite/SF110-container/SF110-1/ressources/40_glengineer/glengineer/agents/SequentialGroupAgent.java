package glengineer.agents;

import java.util.ListIterator;

import glengineer.agents.setters.*;

import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.GroupLayout.*;

/**
 * �����, �������������� ���������������� ������.
 */
public class SequentialGroupAgent
	extends GroupAgent
{
	/**
	 * ���������� �������������� ���������� � ����� ���������������� ������
	 * � ���������� �.
	 * 
	 * @param creatorAndAdder	��������� ��� �������� �����
	 * 							� ���������� � ��� �����������
	 * @return					������, ������������ �� ���������������
	 * 							�����������.
	 */
	public SequentialGroup groupContent(
			GroupCreatorAndElementAdder creatorAndAdder )
	{
		SequentialGroup result = creatorAndAdder.createSequentialGroup();
		for (Agent child : childrenAgents)
			child.addContentToGroup(result, creatorAndAdder);
		return result;
	}
	
	
	public FunctionsOnGroup getFunctionsOnGroupImplemenation()
	{
		return getFunctionsOnSequentialGroupImplemenation();
	}
	
	public FunctionsOnSequentialGroup
		getFunctionsOnSequentialGroupImplemenation()
	{
		return new FunctionsOnSequentialGroupImplemenation();
	}
	
	protected class FunctionsOnSequentialGroupImplemenation
		implements FunctionsOnSequentialGroup
	{
		public FunctionsOnSequentialGroupAndElement
				getComponent(String name)
		{
			return new FunctionsOnSequentialGroupAndElementImplementation(
					SequentialGroupAgent.this,
					SequentialGroupAgent.this.getComponent(name) );
		}
		
		public FunctionsOnSequentialGroupAndElement
			getGroup( String firstName, String lastName )
		{
			return new FunctionsOnSequentialGroupAndElementImplementation(
					SequentialGroupAgent.this,
					SequentialGroupAgent.this.getGroup(firstName, lastName) );
		}
	}
	
	private static class FunctionsOnSequentialGroupAndElementImplementation
		extends FunctionsOnGroupAndElementImplementation
		implements FunctionsOnSequentialGroupAndElement
	{
		public FunctionsOnSequentialGroupAndElementImplementation(
				GroupAgent parent, Agent element )
		{
			super(parent,element);
		}
		
		private SequentialGroupAgent getParentGroupAgent()
		{
			return (SequentialGroupAgent)parentGroupAgent;
		}
		
		/**��������� ����� ��������� ������������ ������
		 * "���������" ����������.*/
		public void addPrecedingRelatedGap()
		{
			addPrecedingRelatedGap(
					GroupLayout.DEFAULT_SIZE,
					GroupLayout.DEFAULT_SIZE );
		}
		/**��������� ����� ��������� ������������ ������
		 * "�����������" ����������.*/
		public void addPrecedingUnrelatedGap()
		{
			addPrecedingUnrelatedGap(
					GroupLayout.DEFAULT_SIZE,
					GroupLayout.DEFAULT_SIZE );
		}

		/**��������� ����� �������� ������������ ������
		 * "���������" ����������.*/
		public void addFollowingRelatedGap()
		{
			addFollowingRelatedGap(
					GroupLayout.DEFAULT_SIZE,
					GroupLayout.DEFAULT_SIZE );
		}
		/**��������� ����� �������� ������������ ������
		 * "�����������" ����������.*/
		public void addFollowingUnrelatedGap()
		{
			addFollowingUnrelatedGap(
					GroupLayout.DEFAULT_SIZE,
					GroupLayout.DEFAULT_SIZE );
		}
		
		/**��������� � ������������ ������ ����� � ���������
		 * "���������" ���������� � ������������� ���������� ��� ����
		 * �������������� � ������������ �������.*/
		public void addPrecedingRelatedGap(int pref, int max)
		{
			getParentGroupAgent()
				.addPreferredGapBefore(
					new PreferredGapAgent(
						LayoutStyle.ComponentPlacement.RELATED, pref, max ),
					elementAgent );
		}
		/**��������� � ������������ ������ ����� � ���������
		 * "�����������" ���������� � ������������� ���������� ��� ����
		 * �������������� � ������������ �������.*/
		public void addPrecedingUnrelatedGap(int pref, int max)
		{
			getParentGroupAgent()
				.addPreferredGapBefore(
					new PreferredGapAgent(
						LayoutStyle.ComponentPlacement.UNRELATED, pref, max ),
					elementAgent );
		}
		
		/**��������� � ������������ ������ ����� � ��������
		 * "���������" ���������� � ������������� ���������� ��� ����
		 * �������������� � ������������ �������.*/
		public void addFollowingRelatedGap(int pref, int max)
		{
			getParentGroupAgent()
				.addPreferredGapAfter(
					new PreferredGapAgent(
						LayoutStyle.ComponentPlacement.RELATED, pref, max ),
					elementAgent );
		}
		/**��������� � ������������ ������ ����� � ��������
		 * "�����������" ���������� � ������������� ���������� ��� ����
		 * �������������� � ������������ �������.*/
		public void addFollowingUnrelatedGap(int pref, int max)
		{
			getParentGroupAgent()
				.addPreferredGapAfter(
					new PreferredGapAgent(
						LayoutStyle.ComponentPlacement.UNRELATED, pref, max ),
					elementAgent );
		}
	}
	
	/**
	 * Finds the specified element in the list of
	 * this sequential group's children and inserts
	 * the specified preferred gap into that list before that element.
	 */
	public void addPreferredGapBefore(PreferredGapAgent gap, Agent element)
	{
		if( childrenAgents.isEmpty() ) {
			childrenAgents.add(gap);
			return;
		}
		ListIterator<Agent> iter = childrenAgents.listIterator();
		while ( iter.hasNext() )
			if( iter.next().equals(element)) {
				iter.previous();
				iter.add(gap);
				return;
			}
		throw new IllegalArgumentException(
				"the " + element.toString() +
				" in the " + toString() + " not found" );
	}
	
	/**
	 * Finds the specified element in the list of
	 * this sequential group's children and inserts
	 * the specified preferred gap into that list after that element.
	 */
	public void addPreferredGapAfter(PreferredGapAgent gap, Agent element)
	{
		if( childrenAgents.isEmpty() ) {
			childrenAgents.add(gap);
			return;
		}
		ListIterator<Agent> iter = childrenAgents.listIterator();
		while ( iter.hasNext() )
			if( iter.next().equals(element)) {
				iter.add(gap);
				return;
			}
		throw new IllegalArgumentException(
				"the " + element.toString() +
				" in the " + toString() + " not found" );
	}
	
	public String toString()
	{
		return "sequential group <" +
			getFirstName() + "," + getLastName() + ">";
	}
}
