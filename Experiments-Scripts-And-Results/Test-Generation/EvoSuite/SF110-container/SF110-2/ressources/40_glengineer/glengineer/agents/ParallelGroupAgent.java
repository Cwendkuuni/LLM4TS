package glengineer.agents;

import glengineer.agents.setters.FunctionsOnGroup;
import glengineer.agents.setters.FunctionsOnParallelGroup;
import glengineer.agents.setters.FunctionsOnParallelGroupAndElement;
import glengineer.agents.settings.ParallelGroupSettings;

import java.util.Iterator;

import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;

/**
 * �����, �������������� ������������ ������.
 */
public class ParallelGroupAgent
	extends GroupAgent
{
	public ParallelGroupAgent()
	{
		settings = new ParallelGroupSettings();
	}
	
	public ParallelGroupSettings getSettings()
	{
		return (ParallelGroupSettings)settings;
	}
	
	/**
	 * Verifies whether all of the elements of this parallel group agent
	 * are gaps.
	 */
	public boolean containsGapsOnly()
	{
		for (Agent child : childrenAgents)
			if( ! child.isGap() )
				return false;
		return true;
	}

	/**
	 * Removes all gaps from this parallel group.
	 */
	public void removeAllGaps()
	{
		Iterator<Agent> i = childrenAgents.iterator();
		while (i.hasNext())
			if( i.next().isGap() )
				i.remove();
	}

	/**
	 * Returns the gap of the type contained in this parallel group
	 * or null if this group has no gaps.
	 * If the gaps possess different types, throws a runtime exception.
	 * <p>
	 * After calling this method this parallel group,
	 * which typically consists of gaps only,
	 * can be replaced by the gap returned by this method.
	 */
	public Agent getGapsType()
	{
		Iterator<Agent> i = childrenAgents.iterator();
		Agent firstGapFound = null;
		Agent next;
		while (i.hasNext()) {
			next = i.next();
			if( ! next.isGap() )  continue;
			if( firstGapFound == null )
				firstGapFound = next;
			else //the first gap has been already found.
				if( next != firstGapFound )
					throw new IllegalArgumentException(
							"A parallel group consists of gaps only "
							+ "but the gaps have different types.");
		}
		return firstGapFound;
	}

	/**
	 * ���������� �������������� ���������� � ����� ������������ ������
	 * � ���������� �.
	 * 
	 * @param creatorAndAdder	��������� ��� �������� �����
	 * 							� ���������� � ��� �����������
	 * @return					������, ������������ �� ���������������
	 * 							�����������.
	 */
	public ParallelGroup groupContent(
			GroupCreatorAndElementAdder creatorAndAdder )
	{
		ParallelGroup result =
			creatorAndAdder.createParallelGroup( getSettings() );
		for (Agent child : childrenAgents)
			child.addContentToGroup(result, creatorAndAdder);
		return result;
	}
	
	
	public FunctionsOnGroup getFunctionsOnGroupImplemenation()
	{
		return getFunctionsOnParallelGroupImplemenation();
	}
	
	public FunctionsOnParallelGroup
		getFunctionsOnParallelGroupImplemenation()
	{
		return new FunctionsOnParallelGroupImplemenation();
	}
	
	private class FunctionsOnParallelGroupImplemenation
		implements FunctionsOnParallelGroup
	{
		/**
		 * ������������� ������ ������������ ����������� ������������ ������.
		 * 
		 * @param alignment	������ ������������
		 */
		public void setContentAlignment(Alignment alignment)
		{
			ParallelGroupAgent.this
					.getSettings().contentAlignment = alignment;
		}

		/**
		 * Determines the {@code resize} behavior of the {@code ParallelGroup}.
		 * 
		 * @param resizable	{@code true} if the group is resizable;
		 * 					if the group is not resizable
		 * 					the preferred size is used for
		 * 					the minimum and maximum size of the group.
		 */
		public void setResizable(boolean resizable)
		{
			ParallelGroupAgent.this
					.getSettings().resizable = resizable; 
		}

		/**
		 * ������� ����� "���������������� �����" ������������ ������
		 * ��������� � ��������� ���������.
		 * <p>
		 * ���������� ��������� {@code FunctionsOnParallelGroupAndElement}
		 * ��������� ������������ �������� � ������������ ������.
		 * 
		 * @param componentName	�������� �������� ����������
		 * @return				���������
		 * 						{@code FunctionsOnParallelGroupAndElement}
		 * 						��������� ������������ ��������
		 */
		public FunctionsOnParallelGroupAndElement
			getComponent(String componentName)
		{
			return new FunctionsOnParallelGroupAndElementImplementation(
					ParallelGroupAgent.this,
					ParallelGroupAgent.this.getComponent(componentName) );
		}

		/**
		 * ������� ����� "���������������� �����" ������������ ������
		 * ��������� � ���������� ���������� ������� � ���������� �����������.
		 * <p>
		 * ���������� ��������� {@code FunctionsOnParallelGroupAndElement}
		 * ��������� ������������ �������� � ������������ ������.
		 * 
		 * @param firstName	�������� ������� ���������� ������� ������
		 * @param lastName	�������� ���������� ���������� ������� ������
		 * @return			���������
		 * 					{@code FunctionsOnParallelGroupAndElement}
		 * 					��������� ������������ ��������
		 */
		public FunctionsOnParallelGroupAndElement
			getGroup(String firstName, String lastName)
		{
			return new FunctionsOnParallelGroupAndElementImplementation(
					ParallelGroupAgent.this,
					ParallelGroupAgent.this.getGroup(firstName,lastName) );
		}
	}
	
	private static class FunctionsOnParallelGroupAndElementImplementation
		extends FunctionsOnGroupAndElementImplementation
		implements FunctionsOnParallelGroupAndElement
	{
		public FunctionsOnParallelGroupAndElementImplementation(
				GroupAgent parent, Agent element )
		{
			super(parent,element);
		}
		
		/**
		 * Assigns the specified alignment of the element {@code elementAgent}
		 * in the parallel group {@code parentAgent}.
		 */
		public void setAlignmentWithinParent(Alignment alignment)
		{
			elementAgent.settings.alignmentWithRespectToParent = alignment;
		}
	}
	
	public String toString() {
		return "parallel group <" +
				getFirstName() + "," + getLastName() + ">";
	}
	
}
