import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import geo.google.*;
import geo.google.datamodel.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.apache.commons.collections.*;
import org.junit.Test;

import java.util.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        // Setup
        GeoAddress addr = mock(GeoAddress.class);
        AddressDetails.Country country = mock(AddressDetails.Country.class);
        AdministrativeArea adminArea = mock(AdministrativeArea.class);
        AdministrativeArea.SubAdministrativeArea subAdminArea = mock(AdministrativeArea.SubAdministrativeArea.class);
        Locality locality = mock(Locality.class);
        PostalCode postalCode = mock(PostalCode.class);
        Thoroughfare thoroughfare = mock(Thoroughfare.class);
        
        // Mock Country
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryNameCode = mock(AddressDetails.Country.CountryNameCode.class);
        when(countryNameCode.getContent()).thenReturn("USA");
        countryNames.add(countryNameCode);
        when(country.getCountryNameCode()).thenReturn(countryNames);
        
        // Mock AdministrativeArea
        List<AdministrativeArea.AdministrativeAreaName> adminAreaNames = new ArrayList<>();
        AdministrativeArea.AdministrativeAreaName adminAreaName = mock(AdministrativeArea.AdministrativeAreaName.class);
        when(adminAreaName.getContent()).thenReturn("California");
        adminAreaNames.add(adminAreaName);
        when(adminArea.getAdministrativeAreaName()).thenReturn(adminAreaNames);
        
        // Mock SubAdministrativeArea
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subAdminAreaNames = new ArrayList<>();
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName = mock(AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName.class);
        when(subAdminAreaName.getContent()).thenReturn("County");
        subAdminAreaNames.add(subAdminAreaName);
        when(subAdminArea.getSubAdministrativeAreaName()).thenReturn(subAdminAreaNames);
        when(adminArea.getSubAdministrativeArea()).thenReturn(subAdminArea);
        
        // Mock Locality
        List<Locality.LocalityName> localityNames = new ArrayList<>();
        Locality.LocalityName localityName = mock(Locality.LocalityName.class);
        when(localityName.getContent()).thenReturn("City");
        localityNames.add(localityName);
        when(locality.getLocalityName()).thenReturn(localityNames);
        when(locality.getPostalCode()).thenReturn(postalCode);
        when(locality.getThoroughfare()).thenReturn(thoroughfare);
        
        // Mock PostalCode
        List<PostalCode.PostalCodeNumber> postalCodeNumbers = new ArrayList<>();
        PostalCode.PostalCodeNumber postalCodeNumber = mock(PostalCode.PostalCodeNumber.class);
        when(postalCodeNumber.getContent()).thenReturn("12345");
        postalCodeNumbers.add(postalCodeNumber);
        when(postalCode.getPostalCodeNumber()).thenReturn(postalCodeNumbers);
        
        // Mock Thoroughfare
        List<ThoroughfareNameType> thoroughfareNames = new ArrayList<>();
        ThoroughfareNameType thoroughfareName = mock(ThoroughfareNameType.class);
        when(thoroughfareName.getContent()).thenReturn("Street 1");
        thoroughfareNames.add(thoroughfareName);
        when(thoroughfare.getThoroughfareName()).thenReturn(thoroughfareNames);
        
        // Mock AddressDetails
        AddressDetails addressDetails = mock(AddressDetails.class);
        when(addressDetails.getCountry()).thenReturn(country);
        when(addr.getAddressDetails()).thenReturn(addressDetails);
        
        // Mock Coordinate
        Coordinate coordinate = mock(Coordinate.class);
        when(addr.getCoordinate()).thenReturn(coordinate);
        
        // Mock Accuracy
        when(addr.getAccuracy()).thenReturn(0.87);
        
        // Execution
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(addr);
        
        // Verification
        assertEquals("USA", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("County", result.getCounty());
        assertEquals("City", result.getCity());
        assertEquals("12345", result.getPostalCode());
        assertEquals("Street 1", result.getAddressLine1());
        assertNull(result.getAddressLine2());
        verify(addr, times(1)).getCoordinate();
        verify(addr, times(1)).getAccuracy();
    }
}