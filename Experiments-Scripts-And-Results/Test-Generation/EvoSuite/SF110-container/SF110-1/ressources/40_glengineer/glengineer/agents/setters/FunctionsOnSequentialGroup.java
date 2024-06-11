package glengineer.agents.setters;

public interface FunctionsOnSequentialGroup
		extends FunctionsOnGroup
{
	/**
	 * ������� ����� "���������������� �����" ���������������� ������
	 * ��������� � ��������� ���������.
	 * <p>
	 * ���������� ��������� {@code PrefGapsAdderAroundElement}
	 * ���������� ���������� ����� ��� ����� ��������� ���������
	 * � ������ ���������������� ������.
	 * 
	 * @param componentName	�������� �������� ����������
	 * @return				��������� {@code PrefGapsAdderAroundElement}
	 * 						���������� ����������
	 */
	public FunctionsOnSequentialGroupAndElement
		getComponent(String name);
	
	/**
	 * ������� ����� "���������������� �����" ���������������� ������
	 * ������, ������� ��������� �������� ������� � ���������� �����������.
	 * <p>
	 * ���������� ��������� {@code PreferredGapsAdderAroundElement}
	 * ���������� ���������� ����� ��� ����� ��������� ������
	 * � ������ ���������������� ������.
	 * 
	 * @param firstName	�������� ������� ���������� ������� ������
	 * @param lastName	�������� ���������� ���������� ������� ������
	 * @return			��������� {@code PrefGapsAdderAroundElement}
	 * 					���������� ����������
	 */
	public FunctionsOnSequentialGroupAndElement
		getGroup(String firstName, String lastName);
	
}
