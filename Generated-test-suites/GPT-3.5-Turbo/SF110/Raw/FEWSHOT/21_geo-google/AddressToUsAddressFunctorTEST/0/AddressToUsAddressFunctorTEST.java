package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

import org.junit.Test;


public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        GeoAddress addr = mock(GeoAddress.class);
        AddressDetails addressDetails = mock(AddressDetails.class);
        AddressDetails.Country country = mock(AddressDetails.Country.class);
        AddressDetails.Country.CountryNameCode countryNameCode = mock(AddressDetails.Country.CountryNameCode.class);
        AdministrativeArea administrativeArea = mock(AdministrativeArea.class);
        AdministrativeArea.AdministrativeAreaName administrativeAreaName = mock(AdministrativeArea.AdministrativeAreaName.class);
        AdministrativeArea.SubAdministrativeArea subAdministrativeArea = mock(AdministrativeArea.SubAdministrativeArea.class);
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdministrativeAreaName = mock(AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName.class);
        Locality locality = mock(Locality.class);
        Locality.LocalityName localityName = mock(Locality.LocalityName.class);
        PostalCode postalCode = mock(PostalCode.class);
        PostalCode.PostalCodeNumber postalCodeNumber = mock(PostalCode.PostalCodeNumber.class);
        Thoroughfare thoroughfare = mock(Thoroughfare.class);
        ThoroughfareNameType thoroughfareNameType = mock(ThoroughfareNameType.class);
        GpsCoordinate gpsCoordinate = mock(GpsCoordinate.class);

        when(addr.getAddressDetails()).thenReturn(addressDetails);
        when(addressDetails.getCountry()).thenReturn(country);
        when(country.getCountryNameCode()).thenReturn(Arrays.asList(countryNameCode));
        when(countryNameCode.getContent()).thenReturn("US");
        when(country.getAdministrativeArea()).thenReturn(administrativeArea);
        when(administrativeArea.getAdministrativeAreaName()).thenReturn(Arrays.asList(administrativeAreaName));
        when(administrativeAreaName.getContent()).thenReturn("California");
        when(administrativeArea.getSubAdministrativeArea()).thenReturn(subAdministrativeArea);
        when(subAdministrativeArea.getSubAdministrativeAreaName()).thenReturn(Arrays.asList(subAdministrativeAreaName));
        when(subAdministrativeAreaName.getContent()).thenReturn("County");
        when(administrativeArea.getLocality()).thenReturn(locality);
        when(locality.getLocalityName()).thenReturn(Arrays.asList(localityName));
        when(localityName.getContent()).thenReturn("City");
        when(locality.getPostalCode()).thenReturn(postalCode);
        when(postalCode.getPostalCodeNumber()).thenReturn(Arrays.asList(postalCodeNumber));
        when(postalCodeNumber.getContent()).thenReturn("12345");
        when(locality.getThoroughfare()).thenReturn(thoroughfare);
        when(thoroughfare.getThoroughfareName()).thenReturn(Arrays.asList(thoroughfareNameType));
        when(thoroughfareNameType.getContent()).thenReturn("Street");

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        GeoUsAddress result = functor.execute(addr);

        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("County", result.getCounty());
        assertEquals("City", result.getCity());
        assertEquals("12345", result.getPostalCode());
        assertEquals("Street", result.getAddressLine1());
        assertNull(result.getAddressLine2());
    }
}